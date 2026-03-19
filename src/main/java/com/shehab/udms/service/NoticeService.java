package com.shehab.udms.service;


import com.shehab.udms.DTO.NoticeDTO;
import com.shehab.udms.model.Notice;
import com.shehab.udms.repo.NoticeRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepo noticeRepo;


    // dto
    private static @NonNull NoticeDTO getDto(Notice savedNotice) {
        return new NoticeDTO(
                savedNotice.getId(),
                savedNotice.getTitle(),
                savedNotice.getContent(),
                savedNotice.getDate(),
                savedNotice.getPostBy(),
                savedNotice.getNoticeForSem(),
                savedNotice.getDepartment()
        );
    }

    public List<NoticeDTO> findAllNotice(){

        return noticeRepo.findAll().stream()
                .map(notice -> getDto(notice))
                .toList();
    }


    // save notice
    public NoticeDTO saveNotice(Notice notice){

        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loggedUsername = auth.getName();

            if (loggedUsername == null) {
                throw new RuntimeException("create");
            }

            notice.setPostBy(loggedUsername);
            notice.setDate(LocalDateTime.now());
            Notice savedNotice = noticeRepo.save(notice);

            return getDto(savedNotice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // update
    public NoticeDTO updateNotice(Long id, Notice updatedNotice) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        if (loggedUsername == null) {
            throw new RuntimeException("null update");
        }

        Notice notice = noticeRepo.findById(id).orElseThrow(()-> new RuntimeException("Notice not found"));

        notice.setTitle(updatedNotice.getTitle());
        notice.setContent(updatedNotice.getContent());
        notice.setDate(LocalDateTime.now());
        notice.setPostBy(loggedUsername);
        notice.setNoticeForSem(updatedNotice.getNoticeForSem());
        notice.setDepartment(updatedNotice.getDepartment());

        noticeRepo.save(notice);
        return getDto(notice);
    }

    // delete
    public void deleteNotice(Long id) {
        noticeRepo.deleteById(id);
    }
    public NoticeDTO finedNotice(Long id) {

        Notice notice = noticeRepo.findById(id).orElseThrow(()-> new RuntimeException("Notice not found"));

        return getDto(notice);
    }
}
