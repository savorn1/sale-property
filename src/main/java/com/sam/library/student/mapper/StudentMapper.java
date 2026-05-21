package com.sam.library.student.mapper;

import com.sam.library.student.dto.StudentDTO;
import com.sam.library.student.entity.Student;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toStudentDTO(Student s) {
        StudentDTO student = new StudentDTO();
        student.setId(s.getId());
        student.setStudentId(s.getStudentId());
        student.setName(s.getName());
        student.setPhoto(s.getPhoto());
        student.setEmail(s.getEmail());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        student.setCreatedAt(s.getCreatedAt().format(formatter));

        student.setUpdatedAt(s.getUpdatedAt().format(formatter));

        return student;
    }

    public Student toStudent(StudentDTO s) {
        Student student = new Student();
        student.setId(s.getId());
        student.setStudentId(s.getStudentId());
        student.setName(s.getName());
        student.setPhoto(s.getPhoto());
        student.setPhoneNumber(s.getPhoneNumber());
        student.setEmail(s.getEmail());

        return student;
    }
}
