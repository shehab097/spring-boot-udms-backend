package com.shehab.udms.repo;

import com.shehab.udms.model.Attendance;
import com.shehab.udms.model.Course;
import com.shehab.udms.model.Semester;
import com.shehab.udms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Long> {
    // 🔍 find by course
    List<Attendance> findByCourseId(Long courseId);

    // 🔍 find by semester
    List<Attendance> findBySemesterId(Long semesterId);

    // 🔍 find by date
    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByCourseIdAndDate(Long courseId, LocalDate date);

    Optional<Attendance> findByStudentAndCourseAndSemesterAndDate(
            Student student, Course course, Semester semester, LocalDate date
    );


    List<Attendance> findByStudentIdAndCourseId(Long userId, Long courseId);

    List<Attendance> findByStudentId(Long userId);
}
