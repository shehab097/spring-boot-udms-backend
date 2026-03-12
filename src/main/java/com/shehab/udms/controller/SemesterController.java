package com.shehab.udms.controller;

import com.shehab.udms.DTO.SemesterDTO;
import com.shehab.udms.model.Semester;
import com.shehab.udms.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/semester")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @GetMapping
    public ResponseEntity<List<SemesterDTO>> getAllSemesters(){
        return ResponseEntity.ok(semesterService.getAllSemesters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SemesterDTO> getSemester(@PathVariable int id){
        return ResponseEntity.ok(semesterService.getSemester(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SemesterDTO> postSemester(@RequestBody Semester semester){
        return ResponseEntity.ok(semesterService.postSemester(semester));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SemesterDTO> putSemester(@PathVariable int id, @RequestBody Semester semester){
        return ResponseEntity.ok(semesterService.updateSemester(id,semester));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSemester(@PathVariable int id){
        semesterService.deleteSemester(id);
        return ResponseEntity.ok().build();
    }
}
