package ir.maktab.finalproject.service;

import ir.maktab.finalproject.exception.InvalidSpecialistStatusException;
import ir.maktab.finalproject.service.dto.input.SpecialistInputDTO;
import ir.maktab.finalproject.service.dto.output.SpecialistOutputDTO;
import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.Specialist;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.AssistanceNotFoundException;
import ir.maktab.finalproject.exception.SpecialistNotFoundException;
import ir.maktab.finalproject.repository.AssistanceRepository;
import ir.maktab.finalproject.repository.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialistService {
    @Autowired
    private SpecialistRepository repository;

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public SpecialistOutputDTO save(SpecialistInputDTO inputDTO){
        Specialist specialist = convertFromDTO(inputDTO);
        Specialist saved = repository.save(specialist);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public Specialist getById(Long customerId){
        Specialist specialist = repository.findById(customerId).orElseThrow(() -> new SpecialistNotFoundException());
        return specialist;
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

    @Transactional(readOnly = true)
    public List<SpecialistOutputDTO> findByFirstName(String firstName, Pageable pageable){
        return repository.findByFirstName(firstName, pageable).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpecialistOutputDTO> findByLastName(String lastName, Pageable pageable){
        return repository.findByLastName(lastName,pageable).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpecialistOutputDTO> findByEmail(String email, Pageable pageable){
        return repository.findByEmail(email, pageable).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpecialistOutputDTO> findByAssistanceId(Long assistanceId , Pageable pageable) {
        Assistance assistance = assistanceRepository.findById(assistanceId).orElseThrow(() -> new AssistanceNotFoundException());
        return  repository.findByAssistanceId(assistance, pageable).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public SpecialistOutputDTO changePassword(Long specialistId , String password){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        specialist.setPassword(encoder.encode(password));
        Specialist saved = repository.save(specialist);
        return convertToDTO(saved);
    }

    @Transactional
    public SpecialistOutputDTO changePhotoUrl(Long specialistId , String url){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        specialist.setPhotoURL(url);
        Specialist saved = repository.save(specialist);
        return convertToDTO(saved);
    }

    @Transactional
    public SpecialistOutputDTO changeStatus(Long specialistId , UserStatus status){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        specialist.setStatus(status);
        Specialist saved = repository.save(specialist);
        return convertToDTO(saved);
    }

    @Transactional
    public SpecialistOutputDTO approve(Long specialistId){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        if (!specialist.getStatus().equals(UserStatus.WAITING_APPROVAL))
            throw new InvalidSpecialistStatusException();
        specialist.setStatus(UserStatus.APPROVED);
        Specialist saved = repository.save(specialist);
        return convertToDTO(saved);
    }

    @Transactional
    public SpecialistOutputDTO update(Long specialistId , SpecialistInputDTO inputDTO){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        specialist.setFirstName(inputDTO.getFirstName());
        specialist.setLastName(inputDTO.getLastName());
        specialist.setEmail(inputDTO.getEmail());
        specialist.setPassword(inputDTO.getPassword());
        specialist.setPhotoURL(inputDTO.getPhotoURL());
        repository.save(specialist);
        return convertToDTO(specialist);
    }

    @Transactional
    public void addAssistance(Long specialistId , Long assistanceId){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        Assistance assistance = assistanceRepository.findById(assistanceId).orElseThrow(() -> new AssistanceNotFoundException());
        specialist.addAssistance(assistance);
        repository.save(specialist);
    }

    @Transactional
    public void removeAssistance(Long specialistId , Long assistanceId){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        Assistance assistance = assistanceRepository.findById(assistanceId).orElseThrow(() -> new AssistanceNotFoundException());
        specialist.removeAssistance(assistance);
        repository.save(specialist);
    }

    @Transactional
    public void removeById(Long specialistId){
        Specialist specialist = repository.findById(specialistId).orElseThrow(() -> new SpecialistNotFoundException());
        repository.delete(specialist);
    }

    public Specialist convertFromDTO(SpecialistInputDTO inputDTO){
        return Specialist.builder()
                .firstName(inputDTO.getFirstName())
                .lastName(inputDTO.getLastName())
                .email(inputDTO.getEmail())
                .password(encoder.encode(inputDTO.getPassword()))
                .registrationDate(new Date())
                .status(UserStatus.NEW)
                .photoURL(inputDTO.getPhotoURL())
                .points(0d)
                .build();
    }

    public SpecialistOutputDTO convertToDTO(Specialist input){
        return SpecialistOutputDTO.builder()
                .id(input.getId())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .registrationDate(input.getRegistrationDate())
                .status(input.getStatus())
                .credit(input.getCredit())
                .photoURL(input.getPhotoURL())
                .points(input.getPoints())
                .role("SPECIALIST")
                .build();
    }
}
