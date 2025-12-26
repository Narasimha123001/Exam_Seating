package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.StudentBulkRegisterRequest;
import com.techtricks.Exam_Seating.dto.StudentRegisterRequest;
import com.techtricks.Exam_Seating.dto.StudentRegisterResponse;
import com.techtricks.Exam_Seating.model.StudentSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface  StudentSessionService {

    public StudentRegisterResponse register(StudentRegisterRequest request);


    public  StudentRegisterResponse bulkRegister(StudentBulkRegisterRequest request);

}
