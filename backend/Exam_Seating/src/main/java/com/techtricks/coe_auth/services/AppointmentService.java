package com.techtricks.coe_auth.services;
import com.techtricks.coe_auth.dtos.AppointmentDto;
import com.techtricks.coe_auth.dtos.AppointmentResponseDto;
import com.techtricks.coe_auth.exceptions.AppointmentException;

import java.util.List;

public interface AppointmentService {

    AppointmentResponseDto bookAppointment(String email, AppointmentDto dto) throws AppointmentException;

    Boolean canBookAppointment(String email);

    List<AppointmentResponseDto> getAllAppointments();

    List<AppointmentResponseDto> getAppointmentById(String email) throws AppointmentException;
}
