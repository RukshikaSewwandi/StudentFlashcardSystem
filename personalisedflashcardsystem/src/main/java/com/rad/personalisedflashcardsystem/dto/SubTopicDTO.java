package com.rad.personalisedflashcardsystem.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubTopicDTO {

    private int subtopicID;
    private String subtopicName;
    private int subjectID;

    public String getSubtopicName() {
        return subtopicName;
    }

    public void setSubtopicName(String subtopicName) {
        this.subtopicName = subtopicName;
    }

    public int getSubtopicID() {
        return subtopicID;
    }

    public void setSubtopicID(int subtopicID) {
        this.subtopicID = subtopicID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }
}
