package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.BlackRoomAccessResponseDto;
import com.techtricks.coe_auth.dtos.BlackRoomAssignDto;
import com.techtricks.coe_auth.exceptions.BlackRoomNotFoundExceptions;
import com.techtricks.coe_auth.exceptions.NoAccessPresentException;
import com.techtricks.coe_auth.exceptions.BlackRoomAccessAlreadyPresentException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlackRoomAccessService {


    BlackRoomAccessResponseDto assignBlackRoomAccess(BlackRoomAssignDto dto) throws IllegalAccessException, BlackRoomNotFoundExceptions, BlackRoomAccessAlreadyPresentException;
    //ToDo -> Optional<RoomAccess> findByUserId(Long id);
    BlackRoomAccessResponseDto findBlackRoomAccess(Long blackRoomId) throws BlackRoomNotFoundExceptions;



//
       void removeBlackRoomAccess(Long RegisterNumber , String blackRoomName) throws NoAccessPresentException, BlackRoomNotFoundExceptions, IllegalAccessException;

     List<BlackRoomAccessResponseDto> findAll();
//
      List<BlackRoomAccessResponseDto> getAccessByRegNo(Long blackRoomAccessId) throws IllegalAccessException;
//

    boolean validateAccess(Long RegisterNumber , Long roomNumber) throws IllegalAccessException, BlackRoomNotFoundExceptions;
}

