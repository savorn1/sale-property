package com.sam.library.student.controller;

import com.sam.library.student.dto.StudentDTO;
import com.sam.library.student.dto.StudentRegisterDTO;
import com.sam.library.student.entity.Student;
import com.sam.library.student.mapper.StudentMapper;
import com.sam.library.student.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/student")
@Tag(name = "Student", description = "Student services APIs")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/get/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id){
        Student student = studentService.getStudentById(id);
        StudentDTO res = studentMapper.toStudentDTO(student);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/get")
    public ResponseEntity<StudentDTO> getStudentByStudentId(@RequestParam(value="studentId") String studentId){
        Student student = studentService.getStudentByStudentId(studentId);
        StudentDTO res = studentMapper.toStudentDTO(student);
        System.out.println(res.getName());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/create")
    public ResponseEntity<String> registerStudent(@RequestBody StudentRegisterDTO request){
        String res = studentService.registerStudent(request.getId(), request.getPassword());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateStudentById(@RequestBody StudentDTO student){
        Student studentEntity = studentMapper.toStudent(student);
        String res = studentService.updateStudentById(studentEntity);
        return ResponseEntity.ok(res);
    }
}
