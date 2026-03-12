package com.shehab.udms.controller;


import com.shehab.udms.DTO.CourseDTO;
import com.shehab.udms.model.Course;
import com.shehab.udms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses(){
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable int id){
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> postCourse(@RequestBody Course course){
        return ResponseEntity.ok(courseService.postCourse(course));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> putCourse(@PathVariable int id, @RequestBody Course course){
        return ResponseEntity.ok(courseService.updateCourse(id,course));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable int id){
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
}
