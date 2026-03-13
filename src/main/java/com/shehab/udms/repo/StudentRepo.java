package com.shehab.udms.repo;

import com.shehab.udms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    Optional<Student> findByUserUsername(String username);

    Optional<Student> findByStudentID(String studentID);
}
