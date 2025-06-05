package com.rad.personalisedflashcardsystem.repo;

import com.rad.personalisedflashcardsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepo extends JpaRepository<Student , Integer> {
    boolean existsByEmail(String email);

    Student findByEmail(String email);

}
