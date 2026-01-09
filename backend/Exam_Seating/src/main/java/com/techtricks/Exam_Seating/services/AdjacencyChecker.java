package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Seat;
import com.techtricks.Exam_Seating.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AdjacencyChecker {

    /**
     * Check if assigning this paper to this seat would create adjacency conflict
     *
     * @param target The seat we're trying to assign
     * @param seatToPaperId Map of position -> paper ID for already assigned seats
     * @param candidatePaperId The paper ID we want to assign
     * @return true if conflict exists (same paper adjacent)
     */
    public boolean hasConflict(Seat target,
                               Map<String, Long> seatToPaperId,
                               Long candidatePaperId) {

        if (candidatePaperId == null) {
            return true;
        }

        int row = target.getRowNo();
        int col = target.getColNo();
        int pos = target.getPosNo();
        Long roomId = target.getRoom() != null ? target.getRoom().getRoomId() : null;

        // Define left and right neighbors only
        // Front/back are allowed, so we don't check row-1 or row+1
        int[][] neighbors = {
                {row, col, pos - 1}, // Left seat
                {row, col, pos + 1}  // Right seat
        };

        for (int[] neighbor : neighbors) {
            String neighborKey = neighbor[0] + "-" + neighbor[1] + "-" + roomId;
            Long neighborPaperId = seatToPaperId.get(neighborKey);

            if (neighborPaperId != null && neighborPaperId.equals(candidatePaperId)) {
                log.trace("Adjacency conflict at {}-{}-{}: neighbor has same paper {}",
                        row, col, pos, candidatePaperId);
                return true;
            }
        }

        return false;
    }
}
