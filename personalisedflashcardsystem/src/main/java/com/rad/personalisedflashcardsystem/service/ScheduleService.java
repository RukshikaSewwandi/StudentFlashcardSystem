package com.rad.personalisedflashcardsystem.service;


import com.rad.personalisedflashcardsystem.dto.FlashCardDTO;
import com.rad.personalisedflashcardsystem.dto.ScheduleDTO;
import com.rad.personalisedflashcardsystem.entity.FlashCard;
import com.rad.personalisedflashcardsystem.entity.Schedule;
import com.rad.personalisedflashcardsystem.repo.FlashCardRepo;
import com.rad.personalisedflashcardsystem.repo.ScheduleRepo;
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
public class ScheduleService {

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private FlashCardRepo flashCardRepo;

    @Autowired
    private ModelMapper modelMapper;

    // Create or update a schedule
    public String saveOrUpdateSchedule(ScheduleDTO scheduleDTO) {
        // Check if the flashcard exists
        Optional<FlashCard> flashCardOptional = flashCardRepo.findById(scheduleDTO.getFlashcardID());
        if (flashCardOptional.isEmpty()) {
            return VarList.RSP_NO_DATA_FOUND; // Flashcard not found
        }

        // Map DTO to entity
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);
        schedule.setFlashCard(flashCardOptional.get());

        // Save or update the schedule
        scheduleRepo.save(schedule);
        return VarList.RSP_SUCCESS;
    }

    // Delete a schedule by ID
    public String deleteSchedule(Long scheduleId) {
        if (scheduleRepo.existsById(scheduleId)) {
            scheduleRepo.deleteById(scheduleId);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND; // Schedule not found
        }
    }

    // Get schedule by flashcard ID
    public ScheduleDTO getScheduleByFlashcardId(int flashcardID) {
        Optional<Schedule> scheduleOptional = scheduleRepo.findByFlashCard_FlashcardID(flashcardID);
        if (scheduleOptional.isPresent()) {
            return modelMapper.map(scheduleOptional.get(), ScheduleDTO.class);
        } else {
            return null; // No schedule found
        }
    }


    // Get flashcards to study today
    public List<FlashCardDTO> getFlashcardsForToday() {
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(1); // Adjust as needed
        List<Schedule> schedulesForToday = scheduleRepo.findSchedulesForToday(currentDate, endDate);
        List<FlashCard> flashcards = flashCardRepo.findFlashCardsBySchedules(schedulesForToday);
        return flashcards.stream()
                .map(flashCard -> modelMapper.map(flashCard, FlashCardDTO.class))
                .collect(Collectors.toList());
    }
}
