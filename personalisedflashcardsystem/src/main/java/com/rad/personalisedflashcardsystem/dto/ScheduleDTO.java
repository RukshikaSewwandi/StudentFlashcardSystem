package com.rad.personalisedflashcardsystem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleDTO {
    private Long id;
    private int flashcardID; // ID of the associated flashcard
    private LocalDate startingDate;
    private int numberOfDays;
    private LocalTime studyTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFlashcardID() {
        return flashcardID;
    }

    public void setFlashcardID(int flashcardID) {
        this.flashcardID = flashcardID;
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
