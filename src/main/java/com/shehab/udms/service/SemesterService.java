package com.shehab.udms.service;

import com.shehab.udms.DTO.SemesterDTO;
import com.shehab.udms.model.Semester;
import com.shehab.udms.repo.SemesterRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepo semesterRepo;

    // dto
    private static @NonNull SemesterDTO getDto(Semester semester) {
        return new SemesterDTO(
                semester.getId(),
                semester.getSemesterNo(),
                semester.getBatch(),
                semester.getSession(),
                semester.getAddedBy(),
                semester.getAddedTime()
        );
    }

    // get all
    public List<SemesterDTO> getAllSemesters(){
        return semesterRepo.findAll().stream()
                .map(
                        semester -> getDto(semester)
                ).toList();
    }

    // get semester
    public SemesterDTO getSemester(int id){

        Semester semester = semesterRepo.findById(id).orElseThrow(() -> new RuntimeException("Semester not found"));

        semesterRepo.save(semester);
        return getDto(semester);
    }

    // post
    public SemesterDTO postSemester(Semester newSemester){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();


        if (loggedUsername == null) {
            throw new RuntimeException("null update");
        }

        newSemester.setAddedBy(loggedUsername);
        newSemester.setAddedTime(LocalDateTime.now());
        Semester semester = semesterRepo.save(newSemester);

        return getDto(semester);
    }

    // update
    public SemesterDTO updateSemester(int id, Semester updatedSemester){

        Semester semester = semesterRepo.findById(id).orElseThrow(() -> new RuntimeException("Semester not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();


        if (loggedUsername == null) {
            throw new RuntimeException("null update");
        }

        // updating
        semester.setSemesterNo(updatedSemester.getSemesterNo());
        semester.setSession(updatedSemester.getSession());
        semester.setAddedBy(loggedUsername);
        semester.setAddedTime(LocalDateTime.now());

        semesterRepo.save(semester); // save
        return getDto(semester);
    }

    //delete
    public void deleteSemester(int id){
        semesterRepo.deleteById(id);
    }

}
