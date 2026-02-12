package com.techtricks.coe_auth.services;

import com.techtricks.coe_auth.dtos.AppointmentDto;
import com.techtricks.coe_auth.dtos.AppointmentResponseDto;
import com.techtricks.coe_auth.exceptions.AppointmentException;
import com.techtricks.coe_auth.exceptions.UserNotFoundException;
import com.techtricks.coe_auth.models.Appointment;
import com.techtricks.coe_auth.models.AppointmentsStatus;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.repositorys.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final int BOOKING_GAP_HOURS = 12;

    private final UserService userService;
    private final AppointmentRepository appointmentRepository;

    @Override
    public AppointmentResponseDto bookAppointment(String email, AppointmentDto dto) throws AppointmentException {

        User user = getUserOrThrow(email);
        validateBookingWindow(user);

        Appointment appointment = Appointment.builder()
                .appointmentDateTime(dto.getAppointmentDateTime())
                .createDate(LocalDateTime.now())
                .purpose(dto.getPurpose())
                .status(AppointmentsStatus.PENDING)
                .user(user)
                .build();

        return toDto(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentById(String email) {
        User user = getUserOrThrow(email);

        return appointmentRepository.findByUser_Id(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Boolean canBookAppointment(String email) {
        User user = getUserOrThrow(email);
        return isBookingAllowed(user);
    }

    @Override
    public List<AppointmentResponseDto> getAllAppointments() {

        return  appointmentRepository
                .findAll()
                .stream()
                .map(this::toDto)  // or use mapper
                .toList();


    }



    private User getUserOrThrow(String email) {
        User user = userService.getUserByEmail(email);
        if (user == null)
            throw new UserNotFoundException("User not found");
        return user;
    }

    private void validateBookingWindow(User user) throws AppointmentException {
        Appointment last = appointmentRepository
                .findTopByUserRegisterNumberOrderByAppointmentDateTimeDesc(
                        user.getRegisterNumber())
                .orElse(null);

        if (last != null &&
                last.getAppointmentDateTime()
                        .plusHours(BOOKING_GAP_HOURS)
                        .isAfter(LocalDateTime.now())) {

            LocalDateTime nextAvailable =
                    last.getAppointmentDateTime().plusHours(BOOKING_GAP_HOURS);

            throw new AppointmentException(
                    "You can book appointment only after: " + nextAvailable
            );
        }
    }

    private boolean isBookingAllowed(User user) {
        Appointment last = appointmentRepository
                .findTopByUserRegisterNumberOrderByAppointmentDateTimeDesc(
                        user.getRegisterNumber())
                .orElse(null);

        return last == null ||
                last.getAppointmentDateTime()
                        .plusHours(BOOKING_GAP_HOURS)
                        .isBefore(LocalDateTime.now());
    }

    private AppointmentResponseDto toDto(Appointment appointment) {
        return new AppointmentResponseDto(
                appointment.getId(),
                appointment.getCreateDate(),
                appointment.getAppointmentDateTime(),
                appointment.getPurpose(),
                appointment.getUser().getRegisterNumber(),
                appointment.getStatus()
        );
    }
}







//        List<Appointment> appointments = appointmentRepository.findByUser_Id(user.getId());
//        List<>
//        if(appointments.isEmpty()){
//            throw new AppointmentException("not Appointment found");
//        }
//        return appointments;


