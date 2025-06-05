package com.rad.personalisedflashcardsystem.repo;

import com.rad.personalisedflashcardsystem.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepo extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByFlashCard_FlashcardID(int flashcardID);

    @Query("SELECT s FROM Schedule s WHERE :currentDate BETWEEN s.startingDate AND :endDate")
    List<Schedule> findSchedulesForToday(@Param("currentDate") LocalDate currentDate, @Param("endDate") LocalDate endDate);
}
