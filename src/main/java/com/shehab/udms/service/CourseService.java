package com.shehab.udms.service;

import com.shehab.udms.DTO.CourseDTO;
import com.shehab.udms.model.Course;
import com.shehab.udms.repo.CourseRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    // dto
    private static @NonNull CourseDTO getDto(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                course.getCourseSemester(),
                course.getCourseCredit(),
                course.getCourseDepartment()
        );
    }

    // get all
    public List<CourseDTO> getAllCourses(){
        return courseRepo.findAll().stream()
                .map(
                        course -> getDto(course)
                ).toList();
    }


    // get course
    public CourseDTO getCourse(int id){
        Course course = courseRepo.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));

        courseRepo.save(course);

        return getDto(course);
    }

    // post
    public CourseDTO postCourse(Course newCourse){

        /*
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();


        if (loggedUsername == null) {
            throw new RuntimeException("null update");
        }
        */

        Course course = courseRepo.save(newCourse);

        return getDto(course);
    }

    // update
    public CourseDTO updateCourse(int id, Course updatedCourse){

        Course course = courseRepo.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));

        // updating
        course.setCourseCode(updatedCourse.getCourseCode());
        course.setCourseName(updatedCourse.getCourseName());
        course.setCourseSemester(updatedCourse.getCourseSemester());
        course.setCourseCredit(updatedCourse.getCourseCredit());
        course.setCourseDepartment(updatedCourse.getCourseDepartment());

        courseRepo.save(course); // save

        return getDto(course);
    }

    //delete
    public void deleteCourse(int id){
        courseRepo.deleteById(id);
    }
}
