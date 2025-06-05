package com.rad.personalisedflashcardsystem.service;

import com.rad.personalisedflashcardsystem.dto.SubTopicDTO;
import com.rad.personalisedflashcardsystem.entity.SubTopic;
import com.rad.personalisedflashcardsystem.entity.Subject;
import com.rad.personalisedflashcardsystem.repo.SubTopicRepo;
import com.rad.personalisedflashcardsystem.repo.SubjectRepo;
import com.rad.personalisedflashcardsystem.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SubTopicService {

    @Autowired
    private SubTopicRepo subTopicRepo;


    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private ModelMapper modelMapper;

    public String saveSubTopic(SubTopicDTO subTopicDTO) {
        if (subTopicRepo.existsById(subTopicDTO.getSubtopicID())) {
            return VarList.RSP_DUPLICATED;
        } else if (subTopicRepo.findBySubtopicName(subTopicDTO.getSubtopicName()).isPresent()) {
            return VarList.RSP_DUPLICATED;
        } else {
            // Fetch the Subject entity using the provided subjectID
            Subject subject = subjectRepo.findById(subTopicDTO.getSubjectID())
                    .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

            // Map the DTO to the SubTopic entity
            SubTopic subTopic = modelMapper.map(subTopicDTO, SubTopic.class);

            // Set the subject in SubTopic
            subTopic.setSubject(subject);

            // Save the SubTopic
            subTopicRepo.save(subTopic);
            return VarList.RSP_SUCCESS;
        }
    }


    public String updateSubTopic(SubTopicDTO subTopicDTO) {
        if(subTopicRepo.existsById(subTopicDTO.getSubtopicID())){
            SubTopic existSubtopic = subTopicRepo.findById(subTopicDTO.getSubtopicID()).orElseThrow(() -> new IllegalArgumentException("Subtopic not found"));

            //update the name
            existSubtopic.setSubtopicName(subTopicDTO.getSubtopicName());

            //Save the updated subtopic
            subTopicRepo.save(existSubtopic);
            return VarList.RSP_SUCCESS;
        }else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    public List<SubTopicDTO> getSubtopicsBySubject(int subjectId) {
        try {
            // Fetch subtopics for the given subject ID from the repository
            List<SubTopic> subtopics = subTopicRepo.findBySubject_SubjectID(subjectId);

            if (subtopics.isEmpty()) {
                throw new RuntimeException("No subtopics found for subject ID: " + subjectId);
            }

            // Map the list of SubTopic entities to a list of SubTopicDTO
            return modelMapper.map(subtopics, new TypeToken<List<SubTopicDTO>>() {}.getType());
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching subtopics: " + ex.getMessage());
        }
    }



    public String deleteSubtopic(int subtopicId) {
        if (subTopicRepo.existsById(subtopicId)) {
            subTopicRepo.deleteById(subtopicId); // Delete the subtopic
            return VarList.RSP_SUCCESS; // Return success code
        } else {
            return VarList.RSP_NO_DATA_FOUND; // Return error code if subtopic not found
        }
    }



}
