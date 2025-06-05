package com.rad.personalisedflashcardsystem.entity;


import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "flashcard_id", nullable = false, unique = true)
    private FlashCard flashCard;

    @Column(nullable = false)
    private LocalDate startingDate; // Starting date of the schedule

    @Column(nullable = false)
    private int numberOfDays; // Number of days to study the flashcard

    @Column(nullable = false)
    private LocalTime studyTime; // Time of the day to study



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlashCard getFlashCard() {
        return flashCard;
    }

    public void setFlashCard(FlashCard flashCard) {
        this.flashCard = flashCard;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public LocalTime getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(LocalTime studyTime) {
        this.studyTime = studyTime;
    }
}
