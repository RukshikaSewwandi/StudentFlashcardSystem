package com.rad.personalisedflashcardsystem.service;

import com.rad.personalisedflashcardsystem.dto.SubTopicDTO;
import com.rad.personalisedflashcardsystem.dto.SubjectDTO;
import com.rad.personalisedflashcardsystem.entity.Subject;
import com.rad.personalisedflashcardsystem.repo.SubjectRepo;
import com.rad.personalisedflashcardsystem.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SubjectService {

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private ModelMapper modelMapper;

    public String saveSubject(SubjectDTO subjectDTO){
        if(subjectRepo.existsById(subjectDTO.getSubjectID())) {

            return VarList.RSP_DUPLICATED;
        }else if(subjectRepo.findBySubjectName(subjectDTO.getSubjectName()).isPresent()) {
            return VarList.RSP_DUPLICATED;
        }
        else{
            subjectRepo.save(modelMapper.map(subjectDTO, Subject.class));
            return VarList.RSP_SUCCESS;
        }
    }



    public String updateSubject(SubjectDTO subjectDTO){
        if(subjectRepo.existsById(subjectDTO.getSubjectID())) {
            subjectRepo.save(modelMapper.map(subjectDTO, Subject.class));
            return VarList.RSP_SUCCESS;
        }else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }


    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjectList = subjectRepo.findAll();
        return modelMapper.map(subjectList, new TypeToken<List<SubjectDTO>>() {}.getType());
    }


    public SubjectDTO searchSubject(String subjectName) {
        return subjectRepo.findBySubjectName(subjectName)
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .orElse(null); // Return null if not found
    }



    public String deleteSubject(int subjectID){
        if (subjectRepo.existsById(subjectID)){
            subjectRepo.deleteById(subjectID);
            return VarList.RSP_SUCCESS;
        }else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }


    public SubjectDTO searchSubject(int subjectId) {
        return subjectRepo.findById(subjectId)
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .orElse(null); // Return null if not found
    }
}
