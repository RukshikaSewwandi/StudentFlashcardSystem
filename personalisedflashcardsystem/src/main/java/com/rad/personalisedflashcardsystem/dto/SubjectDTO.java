package com.rad.personalisedflashcardsystem.dto;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor

public class SubjectDTO {


    private int subjectID;
    private String subjectName;

    private List<SubTopicDTO> subtopics;

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<SubTopicDTO> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<SubTopicDTO> subtopics) {
        this.subtopics = subtopics;
    }
}
