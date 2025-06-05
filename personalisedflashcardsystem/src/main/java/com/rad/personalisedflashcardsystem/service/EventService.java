package com.rad.personalisedflashcardsystem.service;

import com.rad.personalisedflashcardsystem.dto.EventDTO;
import com.rad.personalisedflashcardsystem.entity.Event;
import com.rad.personalisedflashcardsystem.repo.EventRepo;
import com.rad.personalisedflashcardsystem.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    // Create or update an event
    public String saveOrUpdateEvent(EventDTO eventDTO) {
        Event event = modelMapper.map(eventDTO, Event.class);
        eventRepo.save(event);
        return VarList.RSP_SUCCESS;
    }

    // Delete an event by ID
    public String deleteEvent(Long eventId) {
        if (eventRepo.existsById(eventId)) {
            eventRepo.deleteById(eventId);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND; // Event not found
        }
    }

    // Get all events for a specific date
    public List<EventDTO> getEventsByDate(LocalDate eventDate) {
        List<Event> events = eventRepo.findByEventDate(eventDate);
        return events.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    // Get an event by ID
    public EventDTO getEventById(Long eventId) {
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        return eventOptional.map(event -> modelMapper.map(event, EventDTO.class)).orElse(null);
    }


    public List<EventDTO> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Event> events = eventRepo.findByEventDateBetween(startDate, endDate);
        return events.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }
}
