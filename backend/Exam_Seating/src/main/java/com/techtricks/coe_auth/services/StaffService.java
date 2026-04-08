package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.StaffNameDisplay;
import com.techtricks.coe_auth.exceptions.StaffNotPresentException;
import com.techtricks.coe_auth.models.Staff;
import com.techtricks.coe_auth.repositorys.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;


    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    public Staff findByRegistrationNumber(Long registrationNumber) {
        return staffRepository.findByRegisterNumber(registrationNumber);
    }


    public Staff findStaffByEmail(String email) {
        return staffRepository.findByEmail(email);
    }


    public List<StaffNameDisplay> getStaffNames() throws StaffNotPresentException {

        List<Staff> list = staffRepository.findAll();
        if(list.isEmpty()) {
            throw new StaffNotPresentException("No staff Present");
        }
        return list.stream()
                .map(staff ->new StaffNameDisplay(
                        staff.getRegisterNumber(),
                        staff.getName()
                ))
                .toList();
    }
}
