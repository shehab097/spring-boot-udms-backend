package com.shehab.udms.controller;

import com.shehab.udms.model.Teacher;
import com.shehab.udms.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherRepo teacherRepo;

    @GetMapping
    public List<Teacher> getAllTeachers(){
        return teacherRepo.findAll();
    }

    @GetMapping("/{username}")
    public Teacher getTeacherByUsername(@PathVariable String username) {
        return teacherRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }


    @PutMapping("/{username}")
    public Teacher updateTeacher(@PathVariable String username, @RequestBody Teacher updatedTeacher) {

        Teacher teacher = teacherRepo.findByUserUsername(username).orElseThrow(() -> new RuntimeException("Teacher not found"));

        // get the logged-in username from JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        System.out.println(loggedUsername + "##########");

        if (!loggedUsername.equals(username)) {
            throw new RuntimeException("You cannot update another teacher's profile");
        }

        teacher.setName(updatedTeacher.getName());
        teacher.setEmail(updatedTeacher.getEmail());
        teacher.setPhone(updatedTeacher.getPhone());
        teacher.setAddress(updatedTeacher.getAddress());
        teacher.setGender(updatedTeacher.getGender());

        return teacherRepo.save(teacher);
    }

}
