package com.rad.personalisedflashcardsystem.controllers;


import com.rad.personalisedflashcardsystem.dto.ResponseDTO;
import com.rad.personalisedflashcardsystem.dto.SubTopicDTO;
import com.rad.personalisedflashcardsystem.service.SubTopicService;
import com.rad.personalisedflashcardsystem.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/studentflashcard/subtopic")
public class SubTopicController {

    @Autowired
    private SubTopicService subTopicService;

    @Autowired
    private ResponseDTO responseDTO;


    @PostMapping(value = "/saveSubtopic")
    public ResponseEntity saveSubTopic(@RequestBody SubTopicDTO subTopicDTO) {
        try {
            String result = subTopicService.saveSubTopic(subTopicDTO);
            if (result.equals("00")) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Subtopic saved successfully");
                responseDTO.setContent(subTopicDTO);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            } else if (result.equals("06")) {
                responseDTO.setCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Subtopic already exists");
                responseDTO.setContent(subTopicDTO);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Error saving subtopic");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "/updateSubTopic")
    public ResponseEntity updateSubTopic(@RequestBody SubTopicDTO subTopicDTO) {
        try {
            String res = subTopicService.updateSubTopic(subTopicDTO);
            if (res.equals("00")) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Subtopic updated successfully");
                responseDTO.setContent(subTopicDTO);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            } else if (res.equals("01")) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Subtopic not found");
                responseDTO.setContent(subTopicDTO);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Error updating subtopic");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/getSubtopicsBySubject/{subjectId}")
    public ResponseEntity getSubtopicsBySubject(@PathVariable int subjectId) {
        try {
            // Fetch subtopics for the given subject ID
            List<SubTopicDTO> subtopicDTOList = subTopicService.getSubtopicsBySubject(subjectId);

            if (subtopicDTOList.isEmpty()) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No subtopics found for subject ID: " + subjectId);
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);
            }

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(subtopicDTOList);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @DeleteMapping("/deleteSubtopic/{subtopicId}")
    public ResponseEntity deleteSubtopic(@PathVariable int subtopicId) {
        try {
            String result = subTopicService.deleteSubtopic(subtopicId);
            if (result.equals("00")) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Subtopic deleted successfully");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.OK);
            } else if (result.equals("01")) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Subtopic not found");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Error deleting subtopic");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
