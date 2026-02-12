package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.BlackRoomResponse;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.models.BlackRoom;
import com.techtricks.coe_auth.repositorys.BlackRoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlackRoomServiceImpl implements BlackRoomService {

    private final BlackRoomRepository blackRoomRepository;

    public BlackRoomServiceImpl(BlackRoomRepository blackRoomRepository) {
        this.blackRoomRepository = blackRoomRepository;
    }

    @Override
    public BlackRoomResponse addBlackRoom(String blackRoomName) {
        BlackRoom blackRoom = new BlackRoom();
        blackRoom.setBlackRoomName(blackRoomName);
        BlackRoom savedBlackRoom =  blackRoomRepository.save(blackRoom);
        return new  BlackRoomResponse(savedBlackRoom.getBlackRoomId() , savedBlackRoom.getBlackRoomName());
    }

    @Override
    public BlackRoomResponse updateBlackRoom(Long blackRoomId , String blackRoomName) throws BlackRoomNotFoundExceptions {
        Optional<BlackRoom> optionalBlackRoom = blackRoomRepository.findById(blackRoomId);
        if(optionalBlackRoom.isEmpty()) {
            throw new BlackRoomNotFoundExceptions("Room not found");
        }
        BlackRoom blackRoom = optionalBlackRoom.get();
        blackRoom.setBlackRoomName(blackRoomName);
        BlackRoom savedBlackRoom =  blackRoomRepository.save(blackRoom);
        return new BlackRoomResponse(savedBlackRoom.getBlackRoomId() , savedBlackRoom.getBlackRoomName());
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


}
