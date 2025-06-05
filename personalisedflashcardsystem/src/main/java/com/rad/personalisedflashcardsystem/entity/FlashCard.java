package com.rad.personalisedflashcardsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flashcards")
public class FlashCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flashcardID;

    @Column(nullable = false)
    private String flashcardName;

    private boolean isCompleted;
    @Column(columnDefinition = "TEXT")
    private String flashcardContent;


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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getFlashcardContent() {
        return flashcardContent;
    }

    public void setFlashcardContent(String flashcardContent) {
        this.flashcardContent = flashcardContent;
    }

    public SubTopic getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(SubTopic subTopic) {
        this.subTopic = subTopic;
    }

    @ManyToOne
    @JoinColumn(name = "subtopic_id", nullable = false)
    private SubTopic subTopic;


}
