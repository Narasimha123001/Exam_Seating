package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.StudentSession;
import com.techtricks.Exam_Seating.repository.StudentRepository;
import com.techtricks.Exam_Seating.repository.StudentSessionRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BucketServicesImpl implements BucketServices {

    private final StudentSessionRepository studentSessionRepository;
    private final StudentRepository studentRepository;

    public BucketServicesImpl(StudentSessionRepository studentSessionRepository, StudentRepository studentRepository) {
        this.studentSessionRepository = studentSessionRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public Map<Long, Queue<Student>> buildBucketsForSessions(List<Long> sessionIds) {

        Map<Long , Queue<Student>> buckets = new LinkedHashMap<>();

        for( Long sid : sessionIds){
            List<StudentSession> regs = studentSessionRepository
                    .findByExamSessionId(sid);

            //map to student entities

            List<Student> students = regs.stream()
                    .map(StudentSession::getStudent)
                    .filter(Objects::nonNull)
                    .toList();



            List<Student> mutable = new ArrayList<>(students);
            //shuffle to randomize
            Collections.shuffle(mutable);


            Queue<Student> q = new ArrayDeque<>(mutable);
            buckets.put(sid, q);

        }
        return buckets;
    }
}
