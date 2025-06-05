package com.rad.personalisedflashcardsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subtopic")
public class SubTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subtopicID;
    private String subtopicName;

    @ManyToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;

    public int getSubtopicID() {
        return subtopicID;
    }

    public void setSubtopicID(int subtopicID) {
        this.subtopicID = subtopicID;
    }

    public String getSubtopicName() {
        return subtopicName;
    }

    public void setSubtopicName(String subtopicName) {
        this.subtopicName = subtopicName;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
