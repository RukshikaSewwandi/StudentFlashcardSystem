package com.rad.personalisedflashcardsystem.repo;

import com.rad.personalisedflashcardsystem.dto.SubTopicDTO;
import com.rad.personalisedflashcardsystem.entity.SubTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubTopicRepo extends JpaRepository<SubTopic, Integer> {
    Optional<SubTopic> findBySubtopicName(String subtopicName);

    List<SubTopic> findBySubject_SubjectID(int subjectId);
}
