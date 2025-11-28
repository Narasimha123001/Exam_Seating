package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Queue;


@Service
public interface BucketServices {

    public Map<Long , Queue<Student>> buildBucketsForSessions(List<Long> sessionIds);
}
