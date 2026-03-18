package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.BlackRoomRequest;
import com.techtricks.coe_auth.dtos.BlackRoomResponse;
import com.techtricks.coe_auth.dtos.BlackRoomUpdateRequest;
import com.techtricks.coe_auth.exceptions.BlackRoomAlreadyPresentException;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.models.BlackRoom;
import com.techtricks.coe_auth.repositorys.BlackRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlackRoomServiceImpl implements BlackRoomService {

    private final BlackRoomRepository blackRoomRepository;

    public BlackRoomServiceImpl(BlackRoomRepository blackRoomRepository) {
        this.blackRoomRepository = blackRoomRepository;
    }

    @Override
    public BlackRoomResponse addBlackRoom(BlackRoomRequest request)
            throws BlackRoomAlreadyPresentException {

        Optional<BlackRoom> exitingRoom =
                blackRoomRepository.findByBlackRoomName(request.getBlackRoomName());

        if (exitingRoom.isPresent()) {
            throw  new BlackRoomAlreadyPresentException("Room already present");
        }

        BlackRoom blackRoom = new BlackRoom();
        blackRoom.setBlackRoomNumber(request.getBlackRoomNumber());
        blackRoom.setBlackRoomName(request.getBlackRoomName());
        BlackRoom savedBlackRoom =  blackRoomRepository.save(blackRoom);

        return new  BlackRoomResponse(savedBlackRoom.getBlackRoomNumber(), savedBlackRoom.getBlackRoomName());
    }

    @Override
    public BlackRoomResponse updateBlackRoom(BlackRoomUpdateRequest request) throws BlackRoomNotFoundExceptions {
        Optional<BlackRoom> optionalBlackRoom = blackRoomRepository.findByBlackRoomId(request.getBlackRoomId());
        if(optionalBlackRoom.isEmpty()) {
            throw new BlackRoomNotFoundExceptions("Room not found");
        }
        BlackRoom blackRoom = optionalBlackRoom.get();
        blackRoom.setBlackRoomName(request.getBlackRoomName());
        blackRoom.setBlackRoomNumber(request.getBlackRoomNumber());
        BlackRoom savedBlackRoom =  blackRoomRepository.save(blackRoom);
        return new BlackRoomResponse(savedBlackRoom.getBlackRoomNumber(), savedBlackRoom.getBlackRoomName());
    }

    @Override
    public void removeBlackRoom(Long blackRoomId) throws BlackRoomNotFoundExceptions {
        Optional<BlackRoom> optionalBlackRoom = blackRoomRepository.findById(blackRoomId);
        if(optionalBlackRoom.isEmpty()) {
            throw new BlackRoomNotFoundExceptions("BlackRoom not found");
        }
        BlackRoom blackRoom = optionalBlackRoom.get();
        blackRoomRepository.delete(blackRoom);
    }

    @Override
    public BlackRoom findBlackRoom(Long blackRoomId) throws BlackRoomNotFoundExceptions {
        Optional<BlackRoom> optionalBlackRoom = blackRoomRepository.findById(blackRoomId);
        if(optionalBlackRoom.isEmpty()) {
            throw new BlackRoomNotFoundExceptions("BlackRoom not found");
        }
        return optionalBlackRoom.get();
    }

    @Override
    public BlackRoom findBlackRoomByName(String blackRoomName) throws BlackRoomNotFoundExceptions {
        Optional<BlackRoom> optionalBlackRoom = blackRoomRepository.findByBlackRoomName(blackRoomName);
        if(optionalBlackRoom.isEmpty()) {
            throw new BlackRoomNotFoundExceptions("BlackRoom not found");
        }
        return optionalBlackRoom.get();
    }

    @Override
    public BlackRoom findBlackRoomByNumber(Long blackRoomNUmber) throws BlackRoomNotFoundExceptions {
        Optional<BlackRoom> optionalBlackRoom = blackRoomRepository.findByBlackRoomNumber(blackRoomNUmber);
        if(optionalBlackRoom.isEmpty()) {
            throw new BlackRoomNotFoundExceptions("BlackRoom not found");
        }
        return optionalBlackRoom.get();
    }

    @Override
    public List<BlackRoomResponse> getAllBlackRooms() throws BlackRoomNotFoundExceptions {
        List<BlackRoom> blackRooms = blackRoomRepository.findAll();
        if(blackRooms.isEmpty()) {
            throw new BlackRoomNotFoundExceptions("BlackRoom not found");
        }
        return blackRooms.stream()
                .map(room -> new BlackRoomResponse(
                         room.getBlackRoomNumber() , room.getBlackRoomName()
                ))
                .toList();
    }


}
