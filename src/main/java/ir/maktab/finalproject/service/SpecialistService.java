package ir.maktab.finalproject.service;

import ir.maktab.finalproject.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.dto.input.SpecialistInputDTO;
import ir.maktab.finalproject.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.dto.output.SpecialistOutputDTO;
import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.Specialist;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.AssistanceNotFoundException;
import ir.maktab.finalproject.exception.CustomerNotFoundException;
import ir.maktab.finalproject.exception.SpecialistNotFoundException;
import ir.maktab.finalproject.repository.AssistanceRepository;
import ir.maktab.finalproject.repository.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecialistService {

    @Autowired
    SpecialistRepository repository;

    @Autowired
    AssistanceRepository assistanceRepository;

    @Transactional
    public SpecialistOutputDTO save(SpecialistInputDTO inputDTO){
        Specialist specialist = convertFromDTO(inputDTO);
        Specialist saved = repository.save(specialist);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public SpecialistOutputDTO findById(Long specialistId){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        return convertToDTO(specialist);
    }

    @Transactional(readOnly = true)
    public List<SpecialistOutputDTO> findAll(Pageable pageable){
        return repository.findAll(pageable).get().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpecialistOutputDTO> findAll(){
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public SpecialistOutputDTO changePassword(Long specialistId , String password){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        specialist.setPassword(password);
        repository.save(specialist);
        return convertToDTO(specialist);
    }

    public SpecialistOutputDTO update(Long specialistId , SpecialistInputDTO inputDTO){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        specialist.setFirstName(inputDTO.getFirstName());
        specialist.setLastName(inputDTO.getLastName());
        specialist.setEmail(inputDTO.getEmail());
        specialist.setPassword(inputDTO.getPassword());
        specialist.setCredit(inputDTO.getCredit());
        specialist.setStatus(inputDTO.getStatus());
        specialist.setPoints(inputDTO.getPoints());
        specialist.setPhotoURL(inputDTO.getPhotoURL());
        repository.save(specialist);
        return convertToDTO(specialist);
    }

    @Transactional
    public void addAssistance(Long specialistId , Long assistanceId){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new CustomerNotFoundException());
        Assistance assistance = assistanceRepository.findById(assistanceId).orElseThrow(() -> new AssistanceNotFoundException());
        specialist.addAssistance(assistance);
        repository.save(specialist);
    }

    @Transactional
    public void removeAssistance(Long specialistId , Long assistanceId){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new CustomerNotFoundException());
        Assistance assistance = assistanceRepository.findById(assistanceId).orElseThrow(() -> new AssistanceNotFoundException());
        specialist.removeAssistance(assistance);
        repository.save(specialist);
    }

    @Transactional(readOnly = true)
    Page<Specialist> findByFirstName(String firstName, Pageable pageable){
        return ((SpecialistRepository)repository).findByFirstName(firstName, pageable);
    }

    @Transactional(readOnly = true)
    Page<Specialist> findByLastName(String lastName, Pageable pageable){
        return ((SpecialistRepository)repository).findByLastName(lastName,pageable);
    }

    @Transactional(readOnly = true)
    Page<Specialist> findByEmail(String email, Pageable pageable){
        return ((SpecialistRepository)repository).findByEmail(email, pageable);
    }

    @Transactional(readOnly = true)
    Page<Specialist> findByAssistanceId(Assistance assistance , Pageable pageable) {
        return ((SpecialistRepository) repository).findByAssistanceId(assistance, pageable);
    }

    public Specialist convertFromDTO(SpecialistInputDTO inputDTO){
        return Specialist.builder()
                .firstName(inputDTO.getFirstName())
                .lastName(inputDTO.getLastName())
                .email(inputDTO.getEmail())
                .password(inputDTO.getPassword())
                .credit(inputDTO.getCredit())
                .registrationDate(new Date())
                .status(inputDTO.getStatus())
                .points(inputDTO.getPoints())
                .photoURL(inputDTO.getPhotoURL())
                .build();
    }

    public SpecialistOutputDTO convertToDTO(Specialist input){
        return SpecialistOutputDTO.builder()
                .id(input.getId())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getPassword())
                .registrationDate(input.getRegistrationDate())
                .status(input.getStatus())
                .credit(input.getCredit())
                .photoURL(input.getPhotoURL())
                .points(input.getPoints())
                .build();
    }

}
