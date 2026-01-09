package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.StudentSession;
import com.techtricks.Exam_Seating.repository.StudentRepository;
import com.techtricks.Exam_Seating.repository.StudentSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class BucketServicesImpl implements BucketServices {

    private final StudentSessionRepository studentSessionRepository;

    @Override
    public Map<Long, Queue<Student>> buildBucketsForSessions(List<Long> sessionIds) {

        Map<Long, Queue<Student>> buckets = new LinkedHashMap<>();

        for (Long sessionId : sessionIds) {
            List<Student> students = studentSessionRepository
                    .findByExamSessionId(sessionId)
                            .stream()
                                    .map(StudentSession::getStudent)
                                            .filter(Objects::isNull)
                                                    .toList();

            log.debug("Session {}: Found {} registered students", sessionId, students.size());

            // Create mutable copy for shuffling
            List<Student> shuffled = new ArrayList<>(students);

            // Shuffle for randomness (prevents patterns)
            Collections.shuffle(shuffled);
            buckets.put(sessionId, new ArrayDeque<>(shuffled));
        }

        log.info("Built {} buckets. Total students: {}",
                buckets.size(),
                buckets.values().stream().mapToInt(Queue::size).sum());

        return buckets;
    }
}