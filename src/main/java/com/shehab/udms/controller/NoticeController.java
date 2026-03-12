package com.shehab.udms.controller;


import com.shehab.udms.DTO.NoticeDTO;
import com.shehab.udms.model.Notice;
import com.shehab.udms.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;


    @GetMapping
    public ResponseEntity<List<NoticeDTO>> getAllNotice(){

        List<NoticeDTO> dtos = noticeService.findAllNotice();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> postNotice(@PathVariable int id){

        NoticeDTO dto = noticeService.finedNotice(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody Notice notice) {
        return ResponseEntity.ok(noticeService.saveNotice(notice));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<NoticeDTO> putNotice(@PathVariable int id,@RequestBody Notice notice){

        NoticeDTO dto = noticeService.updateNotice(id,notice);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<?> deleteNotice(@PathVariable int id){
        noticeService.deleteNotice(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
