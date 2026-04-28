package com.sam.library.student.service.impl;

import com.sam.library.student.entity.Student;
import com.sam.library.student.repository.StudentRepository;
import com.sam.library.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found."));
    }

    @Override
    public Student getStudentByStudentId(String studentId) {
        return studentRepository.getStudentByStudentId(studentId);
    }

    @Override
    public String registerStudent(Long id, String password) {
        String encryptedPassword = passwordEncoder.encode(password);
        int result = studentRepository.createStudentUser(id, encryptedPassword);
        if (result == 0) throw new RuntimeException("Student not found.");
        return "Profile create Successfully!";
    }

    @Override
    public String updateStudentById(Student student) {
        int result = studentRepository.updateStudent(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPhoto(),
                student.getPhoneNumber());
        if (result == 0) throw new RuntimeException("Student not found.");
        return "Profile Update Successfully!";
    }
}
