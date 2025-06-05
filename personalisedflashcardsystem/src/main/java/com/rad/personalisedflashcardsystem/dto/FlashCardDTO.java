package com.rad.personalisedflashcardsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashCardDTO {
    private int flashcardID;
    private String flashcardName;
    private String flashcardContent;
    private int subtopicID;
    private boolean isCompleted;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getFlashcardID() {
        return flashcardID;
    }

    public void setFlashcardID(int flashcardID) {
        this.flashcardID = flashcardID;
    }

    public String getFlashcardName() {
        return flashcardName;
    }

    public void setFlashcardName(String flashcardName) {
        this.flashcardName = flashcardName;
    }

    public String getFlashcardContent() {
        return flashcardContent;
    }

    public void setFlashcardContent(String flashcardContent) {
        this.flashcardContent = flashcardContent;
    }

    public int getSubtopicID() {
        return subtopicID;
    }

    public void setSubtopicID(int subtopicID) {
        this.subtopicID = subtopicID;
    }
}
