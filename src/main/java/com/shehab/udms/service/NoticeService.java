package com.shehab.udms.service;


import com.shehab.udms.DTO.NoticeDTO;
import com.shehab.udms.model.Notice;
import com.shehab.udms.repo.NoticeRepo;
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



    public List<NoticeDTO> findAllNotice(){

        return noticeRepo.findAll().stream()
                .map(notice -> new NoticeDTO(
                        notice.getId(),
                        notice.getTitle(),
                        notice.getContent(),
                        notice.getDate(),
                        notice.getPostBy(),
                        notice.getNoticeForSem(),
                        notice.getDepartment()
                ))
                .toList();
    }


    // save notice
    public NoticeDTO saveNotice(Notice notice){

        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loggedUsername = auth.getName();

//            System.out.println(loggedUsername + " create");

            if (loggedUsername == null) {
                throw new RuntimeException("create");
            }


//            System.out.println(notice.getContent() +" ::: content");

            notice.setPostBy(loggedUsername);
            notice.setDate(LocalDateTime.now());
            Notice savedNotice = noticeRepo.save(notice);

            return new NoticeDTO(
                    savedNotice.getId(),
                    savedNotice.getTitle(),
                    savedNotice.getContent(),
                    savedNotice.getDate(),
                    savedNotice.getPostBy(),
                    savedNotice.getNoticeForSem(),
                    savedNotice.getDepartment()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    // update
    public NoticeDTO updateNotice(int id, Notice updatedNotice) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

//        System.out.println(loggedUsername + " update");

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

        return new NoticeDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getDate(),
                notice.getPostBy(),
                notice.getNoticeForSem(),
                notice.getDepartment()
        );

    }

    // delete
    public void deleteNotice(int id) {
        noticeRepo.deleteById(id);
    }

    public NoticeDTO finedNotice(int id) {

        Notice notice = noticeRepo.findById(id).orElseThrow(()-> new RuntimeException("Notice not found"));

        return  new NoticeDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getDate(),
                notice.getPostBy(),
                notice.getNoticeForSem(),
                notice.getDepartment()
        );
    }
}
