package com.shehab.udms.controller;


import com.shehab.udms.DTO.AttendanceDTO;
import com.shehab.udms.model.Attendance;
import com.shehab.udms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendence")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceDTO>> getAttendances(){
        return ResponseEntity.ok(attendanceService.getAttendances());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> getAttendance(@PathVariable Long id){
        return ResponseEntity.ok(attendanceService.getAttendance(id));
    }

    // add
    @PostMapping
    public ResponseEntity<AttendanceDTO> postAttendance(@RequestBody AttendanceDTO attendance){
        System.out.println("post call");

        return ResponseEntity.ok(attendanceService.postAttendance(attendance));
    }

    // update
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDTO> putAttendance(@RequestBody AttendanceDTO attendance, @PathVariable Long id){
        System.out.println("put call");
        return ResponseEntity.ok(attendanceService.updateAttendance(attendance, id));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long id){
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<AttendanceDTO>> postBatchAttendance(@RequestBody List<AttendanceDTO> attendances) {
        return ResponseEntity.ok(attendanceService.postAllAttendances(attendances));
    }
    // find by course

    // find be semester

    // find by date


}
