package com.techtricks.coe_auth.services;

import com.techtricks.Exam_Seating.dto.StudentRequest;
import com.techtricks.Exam_Seating.services.StudentService;

import com.techtricks.coe_auth.dtos.UserDto;
import com.techtricks.coe_auth.dtos.UserProfileDto;
import com.techtricks.coe_auth.dtos.UserResponseDto;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.exceptions.UserAlreadyPresentException;
import com.techtricks.coe_auth.models.Role;
import com.techtricks.coe_auth.models.Staff;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.repositorys.UserDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentService  studentService;
    private final StaffService  staffService;



    @Override
    public UserResponseDto saveUser(UserDto dto) throws UserAlreadyPresentException {
        Optional<User> optionalUser = userDetailsRepository.findByUsername(dto.getUsername());
        if(optionalUser.isPresent()){
            throw new UserAlreadyPresentException("user already present");
        }
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRegisterNumber(dto.getRegisterNumber());
            user.setEmail(dto.getEmail());
            user.setName(dto.getName());
            user.setRole(dto.getRole() != null ? dto.getRole() : Role.STUDENT);
            User savedUser = userDetailsRepository.save(user);

        //-> save into student database id user == student
        if(savedUser.getRole()==Role.STUDENT){
            studentResponse(savedUser);
        }

        if(savedUser.getRole()==Role.STAFF){
            staffResponse(savedUser);
        }
        return new UserResponseDto(savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail());
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        return userDetailsRepository.findAll()
                .stream()
                .map(u-> new UserResponseDto(u.getId() ,u.getUsername() , u.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long registerNumber) throws UserNotFoundException {
        Optional<User> optionalUser = userDetailsRepository.findByRegisterNumber(registerNumber);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UserNotFoundException("user not found");
    }
    @Override
    public User findByUserName(String userName) throws UserNotFoundException {
        Optional<User> optionalUser = userDetailsRepository.findByUsername(userName);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UserNotFoundException("user not found");
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        Optional<User>  optionalUser = userDetailsRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UserNotFoundException("user not found");
    }

    @Override
    public UserProfileDto getUSerByEmail(String email) throws UserNotFoundException {
        User user = userDetailsRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("user not found"));

        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                user.getRegisterNumber()
        );
    }

    @Override
    @Transactional
    public User updateUser(Long registerNumber, User user)
            throws UserNotFoundException {
        User existingUser = userDetailsRepository.findByRegisterNumber(registerNumber)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        if (user.getUsername() != null) existingUser.setUsername(user.getUsername());
        if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() != null) existingUser.setRole(user.getRole());
        if (user.getRegisterNumber() != null) existingUser.setRegisterNumber(user.getRegisterNumber());
        return userDetailsRepository.save(existingUser);
    }




    //private methods

    private void staffResponse(User savedUser){
        Staff staff = new Staff();
        staff.setEmail(savedUser.getEmail());
        staff.setName(savedUser.getName());
        staff.setRegisterNumber(savedUser.getRegisterNumber());
        staff.setPhoneNo("9999999999");
        staffService.save(staff);
    }

    private void studentResponse(User savedUser){
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setRegistrationId(savedUser.getRegisterNumber());
        studentRequest.setName(savedUser.getUsername());
        studentRequest.setDepId(1L);   // Default / temp
        studentRequest.setYear(1);
        studentRequest.setSem(1);
        studentRequest.setEmail(savedUser.getEmail());
        studentRequest.setPhone("9999999999");
        studentService.addStudent(studentRequest);
    }
}
