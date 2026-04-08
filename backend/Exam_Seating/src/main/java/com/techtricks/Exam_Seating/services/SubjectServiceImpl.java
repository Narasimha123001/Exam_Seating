package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.SubjectRequest;
import com.techtricks.Exam_Seating.dto.SubjectResponseBack;
import com.techtricks.Exam_Seating.model.Department;
import com.techtricks.Exam_Seating.model.Student;
import com.techtricks.Exam_Seating.model.Subject;
import com.techtricks.Exam_Seating.repository.DepartmentRepository;
import com.techtricks.Exam_Seating.repository.StudentRepository;
import com.techtricks.Exam_Seating.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    public SubjectServiceImpl(SubjectRepository subjectRepository, DepartmentRepository departmentRepository) {
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;

    }

    @Override
    public Subject addSubject(String code, String title, Long deptId, int sem, int parts) {
        Department d = departmentRepository.findDepartmentByCode(code)
                .orElseThrow(() -> new RuntimeException("Dept not found"));

        Subject s = Subject.builder()
                .subjectCode(code)
                .title(title)
                .departments(List.of(d))
                .semester(sem)

                .parts(parts)
                .build();

        return subjectRepository.save(s);
    }

    @Override
    public List<Subject> addSubjectsBulk(List<SubjectRequest> list) {

        List<Subject> subjects = new ArrayList<>();

        for(SubjectRequest s : list){
            List<Department> departments = departmentRepository.findAllById(s.getDepartment());

            Subject subject = Subject.builder()
                    .subjectCode(s.getSubjectCode())
                    .title(s.getTitle())
                    .departments(departments)
                    .semester(s.getSemester())
                    .parts(s.getParts())
                    .year(s.getYear())
                    .build();

            subjects.add(subject);
        }

        return subjectRepository.saveAll(subjects);
    }

    @Override
    public List<SubjectResponseBack> getSubjectsByDept(Long deptId) {
        return subjectRepository.getSubjectByDepartments(deptId).stream()
                .map((s )->{SubjectResponseBack subjectResponse = new SubjectResponseBack();
                subjectResponse.setSubjectCode(s.getSubjectCode());
                subjectResponse.setTitle(s.getTitle());
                subjectResponse.setYear(s.getYear());
                subjectResponse.setSemester(s.getSemester());
                return subjectResponse;
                }).toList();
    }


}
