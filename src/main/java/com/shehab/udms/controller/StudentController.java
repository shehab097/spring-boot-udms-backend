package com.shehab.udms.controller;


import com.shehab.udms.model.Student;
import com.shehab.udms.model.Teacher;
import com.shehab.udms.repo.StudentRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping
    public List<Student> getAllTeachers(){
        return studentRepo.findAll();
    }

    @GetMapping("/{username}")
    public Student getStudentByUsername(@PathVariable String username) {
        return studentRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @PutMapping("/{username}")
    public Student updateStudent(@PathVariable String username, @RequestBody Student updatedStudent) {

        Student student = studentRepo.findByUserUsername(username).orElseThrow(() -> new RuntimeException("Student not found"));

        // get the logged-in username from JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        System.out.println(loggedUsername + "##########");

        if (!loggedUsername.equals(username)) {
            throw new RuntimeException("You cannot update another student's profile");
        }

        student.setStudentID(updatedStudent.getStudentID());
        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        student.setPhone(updatedStudent.getPhone());
        student.setAddress(updatedStudent.getAddress());
        student.setDepartment(updatedStudent.getDepartment());
        student.setGender(updatedStudent.getGender());

        return studentRepo.save(student);
    }
}
