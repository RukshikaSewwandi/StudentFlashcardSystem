package com.rad.personalisedflashcardsystem.controllers;


import com.rad.personalisedflashcardsystem.dto.EventDTO;
import com.rad.personalisedflashcardsystem.dto.ResponseDTO;
import com.rad.personalisedflashcardsystem.service.EventService;
import com.rad.personalisedflashcardsystem.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/studentflashcard/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ResponseDTO responseDTO;

    // Create or update an event
    @PostMapping("/saveEvent")
    public ResponseEntity<String> saveEvent(@RequestBody EventDTO eventDTO) {
        String response = eventService.saveOrUpdateEvent(eventDTO);
        if (response.equals(VarList.RSP_SUCCESS)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Event saved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving event");
        }
    }

    // Delete an event by ID
    @DeleteMapping("/deleteEvent/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        String response = eventService.deleteEvent(eventId);
        if (response.equals(VarList.RSP_SUCCESS)) {
            return ResponseEntity.ok("Event deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }
    }

    // Get all events for a specific date
    @GetMapping("/getEventsByDate/{eventDate}")
    public ResponseEntity<ResponseDTO> getEventsByDate(@PathVariable LocalDate eventDate) {
        List<EventDTO> events = eventService.getEventsByDate(eventDate);
        if (!events.isEmpty()) {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Events retrieved successfully");
            responseDTO.setContent(events);
            return ResponseEntity.ok(responseDTO);
        } else {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("No events found for this date");
            responseDTO.setContent(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    // Get an event by ID
    @GetMapping("/getEventById/{eventId}")
    public ResponseEntity<ResponseDTO> getEventById(@PathVariable Long eventId) {
        EventDTO eventDTO = eventService.getEventById(eventId);
        if (eventDTO != null) {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Event retrieved successfully");
            responseDTO.setContent(eventDTO);
            return ResponseEntity.ok(responseDTO);
        } else {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Event not found");
            responseDTO.setContent(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }


    @GetMapping("/getEventsByDateRange")
    public ResponseEntity<ResponseDTO> getEventsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<EventDTO> events = eventService.getEventsByDateRange(startDate, endDate);
        if (!events.isEmpty()) {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Events retrieved successfully");
            responseDTO.setContent(events);
            return ResponseEntity.ok(responseDTO);
        } else {
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("No events found for this date range");
            responseDTO.setContent(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }
}
