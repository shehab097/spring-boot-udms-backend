package com.shehab.udms.controller;


import com.shehab.udms.DTO.StudentEnrolledDTO;
import com.shehab.udms.service.StudentEnrolledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/student-enrolled")
public class StudentEnrolledController {


    @Autowired
    private StudentEnrolledService enrolledService;


    @GetMapping
    public ResponseEntity<List<StudentEnrolledDTO>> getAllStudentEnrolls(){
        return ResponseEntity.ok(enrolledService.getAllStudentEnrolls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrolledDTO> getStudentEnrolled(@PathVariable int id){
        return ResponseEntity.ok(enrolledService.getStudentEnrolled(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentEnrolledDTO> postStudentEnrolled(@RequestBody StudentEnrolledDTO course){
        return ResponseEntity.ok(enrolledService.postStudentEnrolled(course));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentEnrolledDTO> putStudentEnrolled(@PathVariable int id, @RequestBody StudentEnrolledDTO course){
        return ResponseEntity.ok(enrolledService.updateStudentEnrolled(id,course));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudentEnrolled(@PathVariable int id){
        enrolledService.deleteStudentEnrolled(id);
        return ResponseEntity.ok().build();
    }

}
