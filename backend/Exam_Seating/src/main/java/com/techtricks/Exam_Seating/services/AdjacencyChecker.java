package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Seat;
import com.techtricks.Exam_Seating.model.Student;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdjacencyChecker {

    /**
     * No side-by-side same paper. No diagonals. Front/back allowed.
     */
    public boolean hasConflict(Seat target, Map<String, Student> assigned, Student candidate) {
        if (candidate == null) return true;

        int r = target.getRowNo(), c = target.getColNo(), p = target.getPosNo();

        // left/right only + optional front/back allowed (we allow front/back so we *do not* check r-1/r+1)
        int[][] neighbors = {
                {r, c, p - 1}, // left
                {r, c, p + 1}  // right
                // no diagonals, front/back are allowed so not included
        };

        String candidateBucket = bucketKey(candidate);

        for (int[] n : neighbors) {
            String key = n[0] + "-" + n[1] + "-" + n[2];
            Student neighbor = assigned.get(key);
            if (neighbor == null) continue;

            String neighborBucket = bucketKey(neighbor);
            if (neighborBucket != null && neighborBucket.equals(candidateBucket)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Bucket key used by allocator: paper identity. We use student's examSession id or subject id
     * Here candidate must contain which examSession they're registered for. If not, use student.getCurrentPaperId()
     */
    private String bucketKey(Student s) {
        // If Student holds examSession reference: use it, otherwise return student-specific group (dept-year)
        // Here prefer paper id; you might need to pass paperId in StudentSession if Student object doesn't have it.
        if (s == null) return null;
        // fallback to dept-year if no paper info:
        String dept = s.getDepartment() != null ? s.getDepartment().getCode() : "GEN";
        int year = s.getYear() != 0 ? s.getYear() : s.getSemester();
        return dept + "-" + year;
    }

}