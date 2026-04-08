package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Fee;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.repository.AttendanceRepository;
import com.techtricks.Exam_Seating.repository.FeeRepository;
import com.techtricks.Exam_Seating.repository.ViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EligibilityServiceImpl implements EligibilityService {

    private final AttendanceRepository attendanceRepository;
    private final ViolationRepository violationRepository;
    private final FeeRepository feeRepository;


    @Override
    public boolean isEligible(Student student) {

        boolean attendanceOk  = checkAttendance(student);
        boolean feeOk = checkFee(student);
        boolean isViolation = checkViolation(student);

        return (attendanceOk && feeOk && isViolation);
    }

    private boolean checkAttendance(Student student) {
        return attendanceRepository.findByStudent(student)
                .map(a ->a.getPercentage() >= 75)
                .orElse(false);
    }

    private boolean checkFee(Student student) {
        return feeRepository.findByStudent(student)
                .map(Fee::isPaid)
                .orElse(false);
    }

    private boolean checkViolation(Student student) {
        return violationRepository.findByStudent(student)
                .map(v -> !v.isHasViolation())
                .orElse(true); //assume no violation not found
    }
}
