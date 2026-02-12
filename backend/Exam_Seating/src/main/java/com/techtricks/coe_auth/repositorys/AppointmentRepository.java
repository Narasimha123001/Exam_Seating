package com.techtricks.coe_auth.repositorys;

import com.techtricks.coe_auth.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    List<Appointment> findByUser_Id(Long userId);

     Optional <Appointment> findTopByUserRegisterNumberOrderByAppointmentDateTimeDesc(Long userId);
}
