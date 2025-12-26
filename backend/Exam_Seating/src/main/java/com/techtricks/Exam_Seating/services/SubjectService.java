package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.SubjectRequest;
import com.techtricks.Exam_Seating.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectService {

    public Subject addSubject(String code, String title, Long deptId, int sem, int parts);

    List<Subject> addSubjectsBulk(List<SubjectRequest> list);


}
