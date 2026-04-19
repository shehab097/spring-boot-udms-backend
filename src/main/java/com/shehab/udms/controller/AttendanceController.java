package com.shehab.udms.controller;


import com.shehab.udms.DTO.AttendanceDTO;
import com.shehab.udms.DTO.AttendanceRequestDTO;
import com.shehab.udms.DTO.AttendanceTinyDto;
import com.shehab.udms.model.Attendance;
import com.shehab.udms.service.AttendanceService;
import com.shehab.udms.service.AttendanceWsService;
import com.shehab.udms.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@EnableMethodSecurity
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired private AttendanceService attendanceService;
    @Autowired private AttendanceWsService attendanceWsService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')") // admin and teacher
    public ResponseEntity<List<AttendanceDTO>> getAttendances(){
        return ResponseEntity.ok(attendanceService.getAttendances());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')") // admin and teacher
    public ResponseEntity<AttendanceDTO> getAttendance(@PathVariable Long id){
        return ResponseEntity.ok(attendanceService.getAttendance(id));
    }

    // add
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')") // admin and teacher
    public ResponseEntity<AttendanceDTO> postAttendance(@RequestBody AttendanceDTO attendance){
        System.out.println("post call");

        return ResponseEntity.ok(attendanceService.postAttendance(attendance));
    }

    // update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')") // admin and teacher
    public ResponseEntity<AttendanceDTO> putAttendance(@RequestBody AttendanceDTO attendance, @PathVariable Long id){
        System.out.println("put call");
        return ResponseEntity.ok(attendanceService.updateAttendance(attendance, id));
    }

    // delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')") // admin and teacher
    public ResponseEntity<?> deleteAttendance(@PathVariable Long id){
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')") // admin and teacher
    public ResponseEntity<List<AttendanceDTO>> postBatchAttendance(@RequestBody List<AttendanceDTO> attendances) {
        return ResponseEntity.ok(attendanceService.postAllAttendances(attendances));
    }

    // find by course

    // find be semester

    // find by today and course
    @GetMapping("/today/{courseId}")
    public ResponseEntity<List<AttendanceDTO>> getTodayesAttendance(@PathVariable Long courseId){
        return ResponseEntity.ok(attendanceService.getTodaysAttendanceByCourse(courseId, LocalDate.now()));
    }


    @PostMapping("/scan-qr")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> scanQRCode(@RequestBody AttendanceRequestDTO request) {
        System.out.println("ATTENDANCE:: " + getClass().getName());

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        String resultMessage = attendanceWsService.markAttendanceByQR(request, currentUsername);


        if (resultMessage.contains("successfully")) {
            return ResponseEntity.ok(new ApiResponse(resultMessage));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(resultMessage));
        }
    }

    @GetMapping("/generate-qr/{courseId}/{semesterId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAttendanceQR(@PathVariable Long courseId, @PathVariable Long semesterId) {
        try {
            String base64Image = attendanceWsService.generateTeacherQR(courseId, semesterId);
            return ResponseEntity.ok(Map.of("qrImage", base64Image));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating QR");
        }
    }

    // attandancee
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')") // admin and teacher
    @GetMapping("/attendance-data/{userId}/{courseId}")
    public ResponseEntity<List<AttendanceTinyDto>> getUserByIdAndCourseId(@PathVariable Long userId, @PathVariable Long courseId ){
        return ResponseEntity.ok(attendanceService.getUserByIdAndCourseId(userId, courseId));
    }

    @GetMapping("/attendance-data/{userId}")
    public ResponseEntity<List<AttendanceTinyDto>> getUserByIdAndCourseId(@PathVariable Long userId ){
        return ResponseEntity.ok(attendanceService.getUserById(userId));
    }
}
