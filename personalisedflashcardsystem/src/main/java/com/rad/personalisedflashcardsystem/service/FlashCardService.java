package com.rad.personalisedflashcardsystem.service;

import com.rad.personalisedflashcardsystem.dto.FlashCardDTO;
import com.rad.personalisedflashcardsystem.entity.FlashCard;
import com.rad.personalisedflashcardsystem.entity.SubTopic;
import com.rad.personalisedflashcardsystem.repo.FlashCardRepo;
import com.rad.personalisedflashcardsystem.repo.SubTopicRepo;
import com.rad.personalisedflashcardsystem.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class FlashCardService {

    @Autowired
    private FlashCardRepo flashcardRepository;

    @Autowired
    private SubTopicRepo subtopicRepository;
    @Autowired
    private ModelMapper modelMapper;


    public List<FlashCardDTO> getFlashcardsBySubtopic(int subtopicId) {
        List<FlashCard> flashcards = flashcardRepository.findBySubTopic_SubtopicID(subtopicId);
        return modelMapper.map(flashcards, new TypeToken<List<FlashCardDTO>>() {}.getType());
    }


    // Delete a flashcard

    public String deleteFlashcard(int flashcardID) {
        if (flashcardRepository.existsById(flashcardID)) {
            flashcardRepository.deleteById(flashcardID);
            return VarList.RSP_SUCCESS; // Flashcard deleted successfully
        } else {
            return VarList.RSP_NO_DATA_FOUND; // Flashcard not found
        }
    }

    // Get flashcard by ID
    public String getFlashcardContentById(int flashcardID) {
        Optional<FlashCard> flashcard = flashcardRepository.findById(flashcardID);
        return flashcard.map(FlashCard::getFlashcardContent).orElse(null);
    }


    public String markFlashcardAsCompleted(int flashcardID, boolean isCompleted) {
        Optional<FlashCard> optionalFlashCard = flashcardRepository.findById(flashcardID);
        if (optionalFlashCard.isPresent()) {
            FlashCard flashCard = optionalFlashCard.get();
            flashCard.setCompleted(isCompleted);
            flashcardRepository.save(flashCard);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }


    public String saveFlashcard(FlashCardDTO flashcardDTO) {
        if(flashcardRepository.existsById(flashcardDTO.getFlashcardID())) {
            return VarList.RSP_DUPLICATED;
        }else{
            flashcardRepository.save(modelMapper.map(flashcardDTO, FlashCard.class));
            return VarList.RSP_SUCCESS;
        }
    }

    public String updateFlashcard(FlashCardDTO flashcardDTO) {
        if (flashcardRepository.existsById(flashcardDTO.getFlashcardID())) {
            flashcardRepository.save(modelMapper.map(flashcardDTO, FlashCard.class));
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }
}