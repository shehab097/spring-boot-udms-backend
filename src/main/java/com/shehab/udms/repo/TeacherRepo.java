package com.shehab.udms.repo;

import com.shehab.udms.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUserUsername(String username);

}