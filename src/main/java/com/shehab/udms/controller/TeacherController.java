package com.shehab.udms.controller;

import com.shehab.udms.DTO.TeacherDTO;
import com.shehab.udms.model.Teacher;
import com.shehab.udms.repo.TeacherRepo;
import com.shehab.udms.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private TeacherService teacherService;


    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers(){

        List<TeacherDTO> teachers = teacherService.getAllTeacherDTOs();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TeacherDTO> getTeacherByUsername(@PathVariable String username) {

        TeacherDTO dto = teacherService.getTeacher(username);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{username}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable String username, @RequestBody Teacher updatedTeacher) {

        TeacherDTO dto = teacherService.updateTeacher(username, updatedTeacher);
        return ResponseEntity.ok(dto);
    }

}
