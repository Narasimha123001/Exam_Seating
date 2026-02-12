package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.models.Staff;
import com.techtricks.coe_auth.repositorys.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;


    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }


    public Staff findStaffByEmail(String email) {
        return staffRepository.findByEmail(email);
    }


}
