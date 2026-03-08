package com.shehab.udms.repo;

import com.shehab.udms.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {


    Users findUserByUsername(String username);
}
