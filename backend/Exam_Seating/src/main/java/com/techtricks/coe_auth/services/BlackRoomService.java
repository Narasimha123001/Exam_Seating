package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.BlackRoomRequest;
import com.techtricks.coe_auth.dtos.BlackRoomResponse;
import com.techtricks.coe_auth.dtos.BlackRoomUpdateRequest;
import com.techtricks.coe_auth.exceptions.BlackRoomAlreadyPresentException;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.models.BlackRoom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlackRoomService {

    public BlackRoomResponse addBlackRoom(BlackRoomRequest request) throws BlackRoomAlreadyPresentException;

    public BlackRoomResponse updateBlackRoom(BlackRoomUpdateRequest request) throws BlackRoomNotFoundExceptions;

    public void removeBlackRoom(Long blackRoomId) throws BlackRoomNotFoundExceptions;

    public BlackRoom findBlackRoom(Long blackRoomId) throws BlackRoomNotFoundExceptions;


    public BlackRoom findBlackRoomByName(String blackRoomName) throws BlackRoomNotFoundExceptions;


    public BlackRoom findBlackRoomByNumber(Long blackRoomNumber) throws BlackRoomNotFoundExceptions;

    List<BlackRoomResponse> getAllBlackRooms() throws BlackRoomNotFoundExceptions;


}
