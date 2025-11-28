package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Seat;
import com.techtricks.Exam_Seating.model.SeatHistory;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.repository.SeatHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotationChecker {

    private final SeatHistoryRepository seatHistoryRepository;

    public RotationChecker(SeatHistoryRepository seatHistoryRepository) {
        this.seatHistoryRepository = seatHistoryRepository;
    }

    public boolean violatesRotation(Student student , Seat seat){
        List<SeatHistory> hist = seatHistoryRepository.findTop5ByStudent_StudentIdOrderByHistoryIdDesc(student.getStudentId());
        for( SeatHistory h : hist){
            if(h.getRoom().getRoomId().equals(seat.getRoom().getRoomId())){
                return true;
            }
            if(h.getRowNo() == seat.getRowNo() && h.getColNo() == seat.getColNo() && h.getPosNo() == seat.getPosNo()) {
                return true;
            }
        }
        return false;
    }
}
