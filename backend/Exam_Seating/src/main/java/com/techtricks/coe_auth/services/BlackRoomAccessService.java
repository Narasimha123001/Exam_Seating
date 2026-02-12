package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.BlackRoomAccessResponseDto;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.exceptions.NoAccessPresentException;
import com.techtricks.coe_auth.exceptions.BlackRoomAccessAlreadyPresentException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlackRoomAccessService {


    BlackRoomAccessResponseDto assignBlackRoomAccess(Long RegisterNumber , Long blackRoomId) throws IllegalAccessException, BlackRoomNotFoundExceptions, BlackRoomAccessAlreadyPresentException;
    //ToDo -> Optional<RoomAccess> findByUserId(Long id);
    BlackRoomAccessResponseDto findBlackRoomAccess(Long blackRoomId) throws BlackRoomNotFoundExceptions;



//
      public void removeBlackRoomAccess(Long RegisterNumber , Long blackRoomId) throws NoAccessPresentException, BlackRoomNotFoundExceptions, IllegalAccessException;

    public List<BlackRoomAccessResponseDto> findAll();
//
     public List<BlackRoomAccessResponseDto> getAccessByRegNo(Long blackRoomAccessId) throws IllegalAccessException;
//

    boolean validateAccess(Long RegisterNumber , Long blackRoomId) throws IllegalAccessException;
}

