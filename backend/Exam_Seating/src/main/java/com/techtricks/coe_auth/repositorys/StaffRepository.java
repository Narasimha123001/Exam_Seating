package com.techtricks.coe_auth.repositorys;

import com.techtricks.coe_auth.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Staff findByEmail(String email);
}
