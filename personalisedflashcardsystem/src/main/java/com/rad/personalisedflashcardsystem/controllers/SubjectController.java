package com.rad.personalisedflashcardsystem.controllers;


import com.rad.personalisedflashcardsystem.dto.ResponseDTO;
import com.rad.personalisedflashcardsystem.dto.SubjectDTO;
import com.rad.personalisedflashcardsystem.service.ScheduleService;
import com.rad.personalisedflashcardsystem.service.SubjectService;
import com.rad.personalisedflashcardsystem.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/studentflashcard/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ResponseDTO responseDTO;
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping(value = "/saveSubject")
    public ResponseEntity saveSubject(@RequestBody SubjectDTO subjectDTO) {
        try{
            String res = subjectService.saveSubject(subjectDTO);
            if (res.equals("00")) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Subject saved successfully");
                responseDTO.setContent(subjectDTO);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);

            }else if (res.equals("06")){
                responseDTO.setCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Subject already saved");
                responseDTO.setContent(subjectDTO);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);

            }else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Error");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);

            }
        }catch(Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }



    @PutMapping(value = "/updateSubject")
    public ResponseEntity updateSubject(@RequestBody SubjectDTO subjectDTO) {
        try{
            String res = subjectService.updateSubject(subjectDTO);
            if (res.equals("00")) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Subject saved successfully");
                responseDTO.setContent(subjectDTO);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);

            }else if (res.equals("01")){
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Subject not found");
                responseDTO.setContent(subjectDTO);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);

            }else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Error");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);

            }
        }catch(Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping(value = "/GetAllSubjects")
    public ResponseEntity GetAllSubjects() {
        try{
            List<SubjectDTO> subjectDTOList = subjectService.getAllSubjects(); // Use SubjectDTO
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(subjectDTOList);
            return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);

        }catch (Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;

        }
    }


    @GetMapping("/searchSubject/{subjectName}")
    public ResponseEntity searchSubject(@PathVariable String subjectName) {
        try {
            SubjectDTO subjectDTO = subjectService.searchSubject(subjectName);
            if (subjectDTO != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(subjectDTO);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Subject Available For this subjectName");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND); // Change status to 404
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/deleteSubject/{subjectID}")
    public ResponseEntity deleteSubject(@PathVariable int subjectID){
        try {
            String res = subjectService.deleteSubject(subjectID);
            if (res.equals("00")) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No subject Available");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(e);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{subjectId}")
    public ResponseEntity searchSubject(@PathVariable int subjectId) {
        try {
            SubjectDTO subjectDTO = subjectService.searchSubject(subjectId);
            if (subjectDTO != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(subjectDTO);
                return new ResponseEntity(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Subject not found");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
