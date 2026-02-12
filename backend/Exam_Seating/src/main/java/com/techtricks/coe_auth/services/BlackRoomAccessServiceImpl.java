package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.BlackRoomAccessResponseDto;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.models.Role;
import com.techtricks.coe_auth.models.BlackRoom;
import com.techtricks.coe_auth.models.BlackRoomAccess;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.repositorys.BlackRoomAccessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlackRoomAccessServiceImpl implements BlackRoomAccessService {

    private final BlackRoomAccessRepository blackRoomAccessRepository;
    private final UserService userService;
    private final BlackRoomService blackRoomService;

    public BlackRoomAccessServiceImpl(BlackRoomAccessRepository blackRoomAccessRepository,
                                      UserService userService,
                                      BlackRoomService blackRoomService) {
        this.blackRoomAccessRepository = blackRoomAccessRepository;
        this.userService = userService;
        this.blackRoomService = blackRoomService;


    }

  //TODO -> these private are used for verification and validation purpose

    private BlackRoomAccessResponseDto buildSuccessResponse(BlackRoomAccess savedAccess) {

        return new BlackRoomAccessResponseDto(
                savedAccess.getBlackRoom().getBlackRoomName(),
                savedAccess.getUser().getUsername(),
                savedAccess.getUser().getRegisterNumber()
        );
    }

    private BlackRoomAccess createRoomAccess(User user, BlackRoom blackRoom) {
        BlackRoomAccess blackRoomAccess = new BlackRoomAccess();
        blackRoomAccess.setBlackRoom(blackRoom);
        blackRoomAccess.setUser(user);
        return blackRoomAccess;
    }

    private boolean hasExistingAccess(Long id, Long blackRoomId) {
        return blackRoomAccessRepository.existsByUserIdAndBlackRoomBlackRoomId(id , blackRoomId);
    }

    private BlackRoom validateAndGetRoom(Long blackRoomId) throws BlackRoomNotFoundExceptions {
        BlackRoom blackRoom = blackRoomService.findBlackRoom(blackRoomId);
        if(blackRoom == null) {
            throw new BlackRoomNotFoundExceptions("Room not found for room id: " + blackRoomId);
        }
        return blackRoom;
    }

    private User validateAndGetUser(Long registerNumber) throws IllegalAccessException {
        User user = userService.getUserById(registerNumber);
        if(user == null) {
            throw new UserNotFoundException(
                    String.format("User with id %d not found", registerNumber)
            );
        }
        if(!isStaffMember(user)){
            throw new IllegalAccessException("Only staff members be assigned room access");
        }
        return user;
    }

    private boolean isStaffMember(User user) {
        return user.getRole() !=null  && user.getRole() == Role.STAFF;
    }

    @Override
    public BlackRoomAccessResponseDto findBlackRoomAccess(Long blackRoomId) throws BlackRoomNotFoundExceptions {
        Optional<BlackRoomAccess> roomAccess = blackRoomAccessRepository.findRoomAccessByBlackRoom_BlackRoomId(blackRoomId); //findRoomAccessByBlackRoom_RoomId(roomId);
        if (roomAccess.isEmpty()) {
            throw new BlackRoomNotFoundExceptions("Room access not found for room id: " + blackRoomId);
        }
        User user = roomAccess.get().getUser();
        return new BlackRoomAccessResponseDto(
                roomAccess.get().getBlackRoom().getBlackRoomName(),
                user.getUsername(),
                user.getRegisterNumber()
        );
    }

    @Override
    public BlackRoomAccessResponseDto assignBlackRoomAccess(Long RegisterNumber,
                                                       Long roomId) throws IllegalAccessException, BlackRoomNotFoundExceptions, UserNotFoundException {
        User user = validateAndGetUser(RegisterNumber);
        BlackRoom blackRoom = validateAndGetRoom(roomId);
        if(hasExistingAccess(user.getId() , roomId)){
            return  new BlackRoomAccessResponseDto();
        }
        BlackRoomAccess blackRoomAccess = createRoomAccess(user , blackRoom);
        BlackRoomAccess savedAccess = blackRoomAccessRepository.save(blackRoomAccess);
        return buildSuccessResponse(savedAccess);
    }



    @Override
    @Transactional
    public void removeBlackRoomAccess(Long registerNumber, Long roomId)
            throws UserNotFoundException, BlackRoomNotFoundExceptions, IllegalAccessException {
        User user = validateAndGetUser(registerNumber);
        BlackRoom blackRoom = validateAndGetRoom(roomId);
        if (hasExistingAccess(user.getId(), blackRoom.getBlackRoomId())) {
            blackRoomAccessRepository.deleteRoomAccess(user.getId(), roomId);
        }
    }

    @Override
    public List<BlackRoomAccessResponseDto> findAll() {
        return mapToDtoList(blackRoomAccessRepository.findAll());
    }

    @Override
    public List<BlackRoomAccessResponseDto> getAccessByRegNo(Long registerNumber) throws UserNotFoundException, IllegalAccessException {
        User user = validateAndGetUser(registerNumber);
        return mapToDtoList(blackRoomAccessRepository.findByUser_id(user.getRegisterNumber()));
    }

    @Override
    public boolean validateAccess(Long RegisterNumber ,Long blackRoomId) throws UserNotFoundException, IllegalAccessException {
        User user = validateAndGetUser(RegisterNumber);
        return blackRoomAccessRepository.existsByUserIdAndBlackRoomBlackRoomId(user.getId(), blackRoomId);
    }

    private List<BlackRoomAccessResponseDto> mapToDtoList(List<BlackRoomAccess> blackRoomAccessList) {
        return blackRoomAccessList.stream()
                .map(room -> new BlackRoomAccessResponseDto(
                        room.getBlackRoom().getBlackRoomName(),
                        room.getUser().getUsername(),
                        room.getUser().getRegisterNumber()
                ))
                .collect(Collectors.toList());
    }
}
