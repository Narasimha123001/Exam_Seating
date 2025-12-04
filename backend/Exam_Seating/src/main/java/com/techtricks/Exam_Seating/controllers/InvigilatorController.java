package com.techtricks.Exam_Seating.controllers;

import com.techtricks.Exam_Seating.model.AssignStatus;
import com.techtricks.Exam_Seating.model.Invigilator;
import com.techtricks.Exam_Seating.model.ScanLog;
import com.techtricks.Exam_Seating.model.SeatAssignment;
import com.techtricks.Exam_Seating.repository.InvigilatorRepository;
import com.techtricks.Exam_Seating.repository.ScanLogRepository;
import com.techtricks.Exam_Seating.repository.SeatAssignmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invigilator")
public class InvigilatorController {

    private final SeatAssignmentRepository seatAssignmentRepository;
    private final ScanLogRepository scanLogRepository;
    private final InvigilatorRepository invigilatorRepository;

    public InvigilatorController(SeatAssignmentRepository seatAssignmentRepository,
                                 ScanLogRepository scanLogRepository, InvigilatorRepository invigilatorRepository) {
        this.seatAssignmentRepository = seatAssignmentRepository;
        this.scanLogRepository = scanLogRepository;
        this.invigilatorRepository = invigilatorRepository;
    }

    // FIXED: added @RequestParam Long invigilatorId
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getRoom(
            @PathVariable Long studentId,
            @RequestParam Long sessionId,
            @RequestParam Long invigilatorId
    ) {

        var asg = seatAssignmentRepository.findBySession_SessionId(sessionId)
                .stream()
                .filter(a -> a.getStudent().getStudentId().equals(studentId))
                .findFirst();

        if (asg.isEmpty()) {
            return ResponseEntity.status(404).body("Room Not Found");
        }

        SeatAssignment a = asg.get();
        a.setStatus(AssignStatus.SEATED);
        seatAssignmentRepository.save(a);

        // SAVE SCAN LOG (NOW CORRECT)
        scanLogRepository.save(
                ScanLog.builder()
                        .assignment(a)
                        .invigilator(Invigilator.builder()
                                .invigilatorId(invigilatorId) // NOW RESOLVED
                                .build())
                        .action("SCANNED_IN")
                        .scanTime(java.time.Instant.now().toString())
                        .build()
        );

        return ResponseEntity.ok(
                Map.of(
                        "room", a.getRoom().getName(),
                        "row", a.getSeat().getRowNo(),
                        "col", a.getSeat().getColNo(),
                        "pos", a.getSeat().getPosNo()
                )
        );
    }

    @PostMapping
    public ResponseEntity<Invigilator> create(@RequestBody Invigilator invigilator) {
        return ResponseEntity.ok(invigilatorRepository.save(invigilator));
    }

    @GetMapping
    public ResponseEntity<List<Invigilator>> getAll() {
        return ResponseEntity.ok(invigilatorRepository.findAll());
    }

}