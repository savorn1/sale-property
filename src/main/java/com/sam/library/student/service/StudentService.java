package com.sam.library.student.service;

import com.sam.library.student.entity.Student;

public interface StudentService {
    Student getStudentById(Long id);
    Student getStudentByStudentId(String id);
    String registerStudent(Long id, String password);
    String updateStudentById(Student student);
}
