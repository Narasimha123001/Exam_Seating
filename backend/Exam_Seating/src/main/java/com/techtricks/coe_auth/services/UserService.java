package com.techtricks.coe_auth.services;
import com.techtricks.coe_auth.dtos.UserDto;
import com.techtricks.coe_auth.dtos.UserProfileDto;
import com.techtricks.coe_auth.dtos.UserResponseDto;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.exceptions.UserAlreadyPresentException;
import com.techtricks.coe_auth.models.User;
import org.springframework.stereotype.Service;
import java.util.List;


public interface  UserService {

   // public void save(User user);

    List<UserResponseDto> getAllUser();

    User getUserById(Long registerNumber)throws UserNotFoundException;

    UserResponseDto saveUser(UserDto dto) throws UserAlreadyPresentException;

    public User updateUser(Long registerNumber , User user) throws UserNotFoundException;

    User findByUserName(String userName) throws UserNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;

    UserProfileDto getUSerByEmail(String email) throws UserNotFoundException;
}

