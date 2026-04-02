package com.shehab.udms.controller;


import com.shehab.udms.DTO.AttendanceDTO;
import com.shehab.udms.DTO.AttendanceRequestDTO;
import com.shehab.udms.model.Attendance;
import com.shehab.udms.service.AttendanceService;
import com.shehab.udms.service.AttendanceWsService;
import com.shehab.udms.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/attendence")
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

    // find by date

    @PostMapping("/scan-qr")
    public ResponseEntity<ApiResponse> scanQRCode(@RequestBody AttendanceRequestDTO request) {
        // SecurityContext থেকে লগইন করা স্টুডেন্টের ইউজারনেম নেওয়া হচ্ছে
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // সার্ভিস কল করা
        String resultMessage = attendanceWsService.markAttendanceByQR(request, currentUsername);

        return ResponseEntity.ok(new ApiResponse(resultMessage));
    }

    @GetMapping("/live-status/{courseId}")
    public ResponseEntity<List<AttendanceDTO>> getTodaysLiveAttendance(@PathVariable Long courseId) {
        // আজকের তারিখ
        LocalDate today = LocalDate.now();

        // সার্ভিস থেকে আজকের লিস্ট নিয়ে আসা
        // (এই মেথডটি আপনার AttendanceService-এ আগে থেকেই থাকার কথা)
        List<AttendanceDTO> attendees = attendanceService.getTodaysAttendanceByCourse(courseId, today);

        return ResponseEntity.ok(attendees);
    }
}
