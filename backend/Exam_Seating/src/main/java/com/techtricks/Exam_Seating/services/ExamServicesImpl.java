package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.exception.ExamNotFoundExceptions;
import com.techtricks.Exam_Seating.model.Exam;
import com.techtricks.Exam_Seating.repository.ExamRepository;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamServicesImpl implements ExamService {

    public final ExamRepository examRepository;
    private final UserService userService;

    public ExamServicesImpl(ExamRepository examRepository, UserService userService) {
        this.examRepository = examRepository;
        this.userService = userService;
    }


    @Override
    public Exam createExam(Exam e) {

        Exam exam =  Exam.builder()
                .name(e.getName())
                .examType(e.getExamType())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .createdBy(getName())
                .build();
        return examRepository.save(exam);
    }

    @Override
    public Exam updateExam(Exam exam) throws ExamNotFoundExceptions {

        Optional<Exam> optionalExam= examRepository.findByExamId(exam.getExamId());
        if(optionalExam.isPresent()) {
            Exam updatedExam = optionalExam.get();
            updatedExam.setName(exam.getName());
            updatedExam.setExamType(exam.getExamType());
            updatedExam.setStartDate(exam.getStartDate());
            updatedExam.setEndDate(exam.getEndDate());
            updatedExam.setCreatedBy(getName());
            return examRepository.save(updatedExam);
        }
        else {
            throw  new ExamNotFoundExceptions("Exam not Found");
        }
    }


    private String getName(){
        User user = userService.getUserByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        return user.getName();
    };
}
