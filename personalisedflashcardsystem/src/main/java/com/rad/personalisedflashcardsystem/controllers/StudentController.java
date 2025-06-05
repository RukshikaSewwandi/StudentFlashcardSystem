package com.rad.personalisedflashcardsystem.controllers;

import com.rad.personalisedflashcardsystem.dto.ResponseDTO;
import com.rad.personalisedflashcardsystem.dto.StudentDTO;
import com.rad.personalisedflashcardsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("api/flashcard")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ResponseDTO responseDTO;


    @PostMapping("/signup")
    public ResponseEntity<String> registerStudent(@RequestBody StudentDTO studentDTO) {
        String response = studentService.saveStudent(studentDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginStudent(@RequestBody StudentDTO studentDTO) {
        ResponseDTO response = studentService.loginStudent(studentDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
