package com.shehab.udms.controller;


import com.shehab.udms.DTO.StudentDTO;
import com.shehab.udms.model.Student;
import com.shehab.udms.model.Teacher;
import com.shehab.udms.repo.StudentRepo;
import com.shehab.udms.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private StudentService studentService;


    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents(){

        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{username}")
    public ResponseEntity<StudentDTO> getStudentByUsername(@PathVariable String username) {

        StudentDTO dto = studentService.getStudent(username);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{username}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable String username, @RequestBody Student updatedStudent) {

        StudentDTO dto = studentService.updateStudentDTO(username,updatedStudent);
        return ResponseEntity.ok(dto);
    }
}
