package com.rad.personalisedflashcardsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectID;

    private String subjectName;

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

    public List<SubTopic> getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(List<SubTopic> subTopic) {
        this.subTopic = subTopic;
    }

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
//    @JoinColumn(name = "ss_fk", referencedColumnName = "subjectID")
    private List<SubTopic> subTopic;
}
