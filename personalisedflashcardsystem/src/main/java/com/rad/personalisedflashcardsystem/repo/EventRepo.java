package com.rad.personalisedflashcardsystem.repo;

import com.rad.personalisedflashcardsystem.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {
    List<Event> findByEventDate(LocalDate eventDate);

    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);

}
