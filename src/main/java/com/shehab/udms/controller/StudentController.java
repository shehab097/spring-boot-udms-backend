package com.shehab.udms.controller;


import com.shehab.udms.DTO.StudentDTO;
import com.shehab.udms.DTO.StudentTinyDto;
import com.shehab.udms.model.Student;
import com.shehab.udms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/student")
public class StudentController {

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

    /**
     * Filter students by semester
     * @param semesterId
     * @return
     */
    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<StudentDTO>> getStudentsBySemesterId(@PathVariable Long semesterId) {

        List<StudentDTO> students = studentService.getStudentsBySemester(semesterId);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{username}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable String username, @RequestBody Student updatedStudent) {

        StudentDTO dto = studentService.updateStudentDTO(username,updatedStudent);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}/semester")
    public ResponseEntity<StudentDTO> updateStudentCurrSemester(@PathVariable String username, @RequestBody Student updatedStudent) {

        StudentDTO dto = studentService.updateStudentsCurrSem(username,updatedStudent);
        return ResponseEntity.ok(dto);
    }

    /**
     * Attendance and course id - problem onno batch er data aste pare ki na?
     *
     * @param courseId
     * @return
     */
    @GetMapping("/attendances/{courseId}")
    public ResponseEntity<List<StudentTinyDto>> getAttendanceByCourseId(@PathVariable Long courseId){
        return ResponseEntity.ok(studentService.getStudentByCourseId(courseId));
    }
}
