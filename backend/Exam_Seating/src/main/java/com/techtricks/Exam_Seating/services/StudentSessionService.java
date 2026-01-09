package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.StudentBulkRegisterRequest;
import com.techtricks.Exam_Seating.dto.StudentRegisterRequest;
import com.techtricks.Exam_Seating.dto.StudentRegisterResponse;
import com.techtricks.Exam_Seating.model.StudentSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface  StudentSessionService {

    StudentRegisterResponse register(StudentRegisterRequest request);


    StudentRegisterResponse bulkRegister(StudentBulkRegisterRequest request);

}
