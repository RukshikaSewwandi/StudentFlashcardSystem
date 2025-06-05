package com.rad.personalisedflashcardsystem.repo;

import com.rad.personalisedflashcardsystem.entity.FlashCard;
import com.rad.personalisedflashcardsystem.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FlashCardRepo extends JpaRepository<FlashCard, Integer> {

    Optional<FlashCard> findByFlashcardNameAndSubTopic_SubtopicID(String flashcardName, int subtopicID);

    List<FlashCard> findBySubTopic_SubtopicID(int subtopicID);

    // Add this method to fetch completed flashcards
    List<FlashCard> findByIsCompleted(boolean isCompleted);

    @Query("SELECT f FROM FlashCard f WHERE f IN (SELECT s.flashCard FROM Schedule s WHERE s IN :schedules)")
    List<FlashCard> findFlashCardsBySchedules(@Param("schedules") List<Schedule> schedules);
}
