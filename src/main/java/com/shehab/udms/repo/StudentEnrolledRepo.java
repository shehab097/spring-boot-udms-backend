package com.shehab.udms.repo;

import com.shehab.udms.model.StudentEnrolled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentEnrolledRepo extends JpaRepository<StudentEnrolled, Integer> {

}
