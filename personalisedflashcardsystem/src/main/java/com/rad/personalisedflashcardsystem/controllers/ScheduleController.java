package com.rad.personalisedflashcardsystem.controllers;

import com.rad.personalisedflashcardsystem.dto.FlashCardDTO;
import com.rad.personalisedflashcardsystem.dto.ResponseDTO;
import com.rad.personalisedflashcardsystem.dto.ScheduleDTO;
import com.rad.personalisedflashcardsystem.service.FlashCardService;
import com.rad.personalisedflashcardsystem.service.ScheduleService;
import com.rad.personalisedflashcardsystem.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
    @RestController
    @RequestMapping("/api/studentflashcard/schedule")
    public class ScheduleController {

        @Autowired
        private ScheduleService scheduleService;

        @Autowired
        private ResponseDTO responseDTO;
    @Autowired
    private FlashCardService flashCardService;

    // Create or update a schedule
        @PostMapping("/saveSchedule")
        public ResponseEntity<String> saveSchedule(@RequestBody ScheduleDTO scheduleDTO) {
            String response = scheduleService.saveOrUpdateSchedule(scheduleDTO);
            if (response.equals(VarList.RSP_SUCCESS)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Schedule saved successfully");
            } else if (response.equals(VarList.RSP_NO_DATA_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flashcard not found");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving schedule");
            }
        }

        // Delete a schedule by ID
        @DeleteMapping("/deleteSchedule/{scheduleId}")
        public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId) {
            String response = scheduleService.deleteSchedule(scheduleId);
            if (response.equals(VarList.RSP_SUCCESS)) {
                return ResponseEntity.ok("Schedule deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule not found");
            }
        }

        // Get schedule by flashcard ID
        @GetMapping("/getScheduleByFlashcard/{flashcardID}")
        public ResponseEntity<ResponseDTO> getScheduleByFlashcard(@PathVariable int flashcardID) {
            ScheduleDTO scheduleDTO = scheduleService.getScheduleByFlashcardId(flashcardID);
            if (scheduleDTO != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(scheduleDTO);
                return ResponseEntity.ok(responseDTO);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No schedule found for this flashcard");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
            }
        }


    // Get flashcards to study today
    @GetMapping("/getFlashcardsForToday")
    public ResponseEntity<ResponseDTO> getFlashcardsForToday() {
        try {
            List<FlashCardDTO> flashcards = scheduleService.getFlashcardsForToday();
            if (!flashcards.isEmpty()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Flashcards to study today retrieved successfully");
                responseDTO.setContent(flashcards);
                return ResponseEntity.ok(responseDTO);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No flashcards to study today");
                responseDTO.setContent(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error retrieving flashcards: " + e.getMessage());
            responseDTO.setContent(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
    }
