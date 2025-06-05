package com.rad.personalisedflashcardsystem.repo;

import com.rad.personalisedflashcardsystem.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepo extends JpaRepository<Subject, Integer> {
    Optional<Subject> findBySubjectName(String subjectName);

}
