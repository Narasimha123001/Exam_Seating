//package com.techtricks.Exam_Seating.model;
//
////public enum PaperType {
////    MID , SEM , PRACTICAL
////}
//
//
//
//private int assign(
//        Seat seat,
//        Long bucketId,
//        Map<Long, Queue<Student>> buckets,
//        Map<Long, SeatHistory> cache,
//        Map<Long, Integer> stats) {
//
//    Queue<Student> q = buckets.get(bucketId);
//
//    if (q == null || q.isEmpty()) {
//        return 0;
//    }
//
//    int size = q.size(); // limit to avoid infinite loop
//    List<Student> skipped = new ArrayList<>();
//
//    for (int i = 0; i < size; i++) {
//
//        Student student = q.poll(); // take student
//
//        if (!rotationChecker.violatesRotation(student, seat, cache)) {
//
//            // ✅ assign
//            seatAssignmentRepository.save(
//                    SeatAssignment.builder()
//                            .seat(seat)
//                            .room(seat.getRoom())
//                            .student(student)
//                            .session(ExamSession.builder().sessionId(bucketId).build())
//                            .status(AssignStatus.ASSIGNED)
//                            .assignedAt(Instant.now().toString())
//                            .build()
//            );
//
//            // ✅ save history
//            seatHistoryRepository.save(
//                    SeatHistory.builder()
//                            .student(student)
//                            .examSession(ExamSession.builder().sessionId(bucketId).build())
//                            .seat(seat)
//                            .room(seat.getRoom())
//                            .benchIndex(seat.getBenchIndex())
//                            .rowNo(seat.getRowNo())
//                            .colNo(seat.getColNo())
//                            .posNo(seat.getPosNo())
//                            .build()
//            );
//
//            stats.merge(bucketId, 1, Integer::sum);
//
//            log.trace("Assigned student {} to {} (session {})",
//                    student.getRegisterNo(),
//                    seat.getSeatLabel(),
//                    bucketId);
//
//            // 🔁 put back skipped students
//            q.addAll(skipped);
//
//            return 1;
//        }
//
//        // ❌ failed → keep aside
//        skipped.add(student);
//    }
//
//    // 🔁 put all back if none assigned
//    q.addAll(skipped);
//
//    return 0;
//}