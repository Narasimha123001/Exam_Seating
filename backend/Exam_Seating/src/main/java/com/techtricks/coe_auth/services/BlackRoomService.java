package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.BlackRoomResponse;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.models.BlackRoom;
import org.springframework.stereotype.Service;

@Service
public interface BlackRoomService {

    public BlackRoomResponse addBlackRoom(String blackRoomName);

    public BlackRoomResponse updateBlackRoom(Long blackRoomId , String BlackRoomName) throws BlackRoomNotFoundExceptions;

    public void removeBlackRoom(Long blackRoomId) throws BlackRoomNotFoundExceptions;

    public BlackRoom findBlackRoom(Long blackRoomId) throws BlackRoomNotFoundExceptions;


}
