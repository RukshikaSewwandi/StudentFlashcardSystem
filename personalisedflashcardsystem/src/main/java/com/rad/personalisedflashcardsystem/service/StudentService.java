package com.rad.personalisedflashcardsystem.service;

import com.rad.personalisedflashcardsystem.dto.ResponseDTO;
import com.rad.personalisedflashcardsystem.dto.StudentDTO;
import com.rad.personalisedflashcardsystem.entity.Student;
import com.rad.personalisedflashcardsystem.repo.FlashCardRepo;
import com.rad.personalisedflashcardsystem.repo.StudentRepo;
import com.rad.personalisedflashcardsystem.util.VarList;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




@Service
@Transactional
public class StudentService {
    @ Autowired
    private StudentRepo studentRepo;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private FlashCardRepo flashCardRepo;

    public String saveStudent(StudentDTO studentDTO) {
        if (studentRepo.existsByEmail(studentDTO.getEmail())) {
            return VarList.RSP_DUPLICATED;
        }

        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setPassword(passwordEncoder.encode(studentDTO.getPassword()));

        studentRepo.save(student);
        return VarList.RSP_SUCCESS;
    }

    public ResponseDTO loginStudent(StudentDTO studentDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        Student student = studentRepo.findByEmail(studentDTO.getEmail());

        if (student != null && passwordEncoder.matches(studentDTO.getPassword(), student.getPassword())) {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Login successful");
        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Invalid email or password");
        }

        return responseDTO;
    }




}
