package com.rad.personalisedflashcardsystem.controllers;

import com.rad.personalisedflashcardsystem.dto.FlashCardDTO;
import com.rad.personalisedflashcardsystem.dto.ResponseDTO;
import com.rad.personalisedflashcardsystem.repo.FlashCardRepo;
import com.rad.personalisedflashcardsystem.service.FlashCardService;
import com.rad.personalisedflashcardsystem.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/studentflashcard/flashcard")
public class FlashCardController {

    @Autowired
    private FlashCardService flashcardService;

    @Autowired
    private FlashCardRepo flashCardRepo;

    @Autowired
    private ResponseDTO responseDTO;

    // Save a flashcard
    @PostMapping("/saveFlashcard")
    public ResponseEntity<String> saveFlashcard(@RequestBody FlashCardDTO flashcardDTO) {
        String response = flashcardService.saveFlashcard(flashcardDTO);
        if (response.equals(VarList.RSP_SUCCESS)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Flashcard saved successfully");
        } else if (response.equals(VarList.RSP_DUPLICATED)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Flashcard already exists");
        } else if (response.equals(VarList.RSP_NO_DATA_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subtopic not found");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving flashcard");
        }
    }

    // Get all flashcards by subtopic ID
    @GetMapping("/getFlashcardsBySubtopic/{subtopicId}")
    public ResponseEntity<ResponseDTO> getFlashcardsBySubtopic(@PathVariable int subtopicId) {
        try {
            List<FlashCardDTO> flashcards = flashcardService.getFlashcardsBySubtopic(subtopicId);
            if (flashcards != null && !flashcards.isEmpty()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(flashcards);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No flashcards found for this subtopic");
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching flashcards: " + ex.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a flashcard

    @PutMapping("/updateFlashcard")
    public ResponseEntity<String> updateFlashcard(@RequestBody FlashCardDTO flashcardDTO) {
        String response = flashcardService.updateFlashcard(flashcardDTO);
        if (response.equals(VarList.RSP_SUCCESS)) {
            return ResponseEntity.ok("Flashcard updated successfully");
        } else if (response.equals(VarList.RSP_NO_DATA_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flashcard or subtopic not found");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating flashcard");
        }
    }

    // Delete a flashcard

    @DeleteMapping("/deleteFlashcard/{flashcardID}")
    public ResponseEntity<String> deleteFlashcard(@PathVariable int flashcardID) {
        System.out.println("Delete request received for flashcardID: " + flashcardID);
        String response = flashcardService.deleteFlashcard(flashcardID);
        if (response.equals(VarList.RSP_SUCCESS)) {
            return ResponseEntity.ok("Flashcard deleted successfully");
        } else if (response.equals(VarList.RSP_NO_DATA_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flashcard not found");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting flashcard");
        }
    }


    //Get flashcard by ID
    @GetMapping("/content/{flashcardID}")
    public ResponseEntity<ResponseDTO> getFlashcardContentById(@PathVariable int flashcardID) {
        try {
            String flashcardContent = flashcardService.getFlashcardContentById(flashcardID);
            if (flashcardContent != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Flashcard content retrieved successfully");
                responseDTO.setContent(flashcardContent); // Set only the content
                return ResponseEntity.ok(responseDTO);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Flashcard not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
            }
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error retrieving flashcard content: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }


}