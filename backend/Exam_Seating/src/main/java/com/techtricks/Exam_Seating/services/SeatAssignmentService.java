package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.*;
import com.techtricks.Exam_Seating.model.AssignStatus;
import com.techtricks.Exam_Seating.model.ExamSession;
import com.techtricks.Exam_Seating.model.Room;
import com.techtricks.Exam_Seating.model.SeatAssignment;
import com.techtricks.Exam_Seating.repository.SeatAssignmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatAssignmentService {

    private final SeatAssignmentRepository repo;
    private final ExamSessionService sessionService;

    public StudentSeatResponse findByStudent(Long studentId) {
        SeatAssignment sa = repo.findByStudent_StudentId(studentId)
                .orElseThrow();

        return new StudentSeatResponse(
                sa.getStudent().getStudentId(),
                sa.getRoom().getRoomId(),
                sa.getSeat().getSeatId()
        );
    }

    public List<Long> getAllRooms() {
        return repo.findAllUsedRooms();
    }

    public List<SeatAssignmentResponse> getRoomSeats(Long roomId) {
        return repo.findByRoom(roomId).stream()
                .map(sa -> new SeatAssignmentResponse(
                        sa.getAssignmentId(),
                        sa.getStudent().getStudentId(),
                        sa.getRoom().getRoomId(),
                        sa.getSeat().getSeatId(),
                        sa.getSeat().getRowNo(),
                        sa.getSeat().getColNo()
                ))
                .toList();
    }
//
//    /** new method**/
//
//    public SeatRoomResponse getRoomBySessionId(Long sessionId) {
//
//        ExamSession session = sessionService.getById(sessionId);
//        System.out.println(session.getSessionId());
//
//        List<Long> room = repo.findRoomBySession_SessionId(session.getSessionId());
//
//        List<Long> uniqueRoom = room.stream()
//                        .distinct()
//                                .toList();
//        System.out.println(room.size());
//        return new SeatRoomResponse(
//                uniqueRoom,
//                session.getDate(),
//                sessionId
//        ) {
//        };
//    }
//
//    public StudentExamRoomDetails getStudentsByRoomId(Long roomId) {
//
//        List<Long> seat = repo.findSeatByRoom_RoomId(roomId);
//
//
//        return new StudentExamRoomDetails(
//                seat
//
//        );
//    }
//
//
//    public StudentDetailsInExamHall getStudentsBySeatId(Long seatId){
//
//        Long reg = repo.findStudentSeat_SeatId(seatId);
//        return new StudentDetailsInExamHall(
//                reg
//        );
//    }
//new..........

    public List<Long> getRoomsByDateAndSlotNo(String date, String slotCode) {

        return repo.getSessionByDateAndSlot(date , slotCode);
    }

    public SeatRoomResponse getRoomsByDateAndSlot(String date, String slotCode) {

        List<Long> rooms = repo.findRoomsByDateAndSlot(date, slotCode);
        List<Long> ids = getRoomsByDateAndSlotNo(date, slotCode);
        return new SeatRoomResponse(
                rooms,
                date,
                ids // sessionId not needed now
        );
    }
//
//public SeatRoomResponse getRoomBySessionId(Long sessionId) {
//
//    ExamSession session = sessionService.getById(sessionId);
//
//    List<Long> rooms = repo.findRoomBySession_SessionId(sessionId);
//
//    return new SeatRoomResponse(
//            rooms,
//            session.getDate(),
//            sessionId
//    );
//}
//    public List<Long> getSeatsByRoom(Long roomId, Long sessionId) {
//
//        return repo.findSeatByRoomAndSession(roomId, sessionId);
//    }


    public Long getStudentBySeat(Long seatId, Long sessionId) {

        return repo.findStudentBySeatAndSession(seatId, sessionId);
    }


    @Transactional
    public String markAttendance(Long roomId, Long studentId, Long sessionId) {

        SeatAssignment assignment = repo.findByRoomStudentAndSession(
                roomId, studentId, sessionId
        ).orElseThrow(() ->
                new RuntimeException("Student not assigned to this room/session")
        );

        assignment.setStatus(AssignStatus.PRESENT);

        return "Marked PRESENT";
    }

    public List<SeatStatusResponse> getSeatsByRoom(Long roomId, Long sessionId) {
        return repo.findSeatStatusByRoomAndSession(roomId, sessionId);
    }
}