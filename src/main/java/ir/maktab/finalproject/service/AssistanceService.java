package ir.maktab.finalproject.service;

import ir.maktab.finalproject.service.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.service.dto.output.AssistanceOutputDTO;
import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.exception.AssistanceNotFoundException;
import ir.maktab.finalproject.exception.SpecialistNotFoundException;
import ir.maktab.finalproject.repository.AssistanceRepository;
import ir.maktab.finalproject.repository.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssistanceService {

    @Autowired
    AssistanceRepository repository;

    @Autowired
    SpecialistRepository specialistRepository;

    @Transactional
    public AssistanceOutputDTO save(AssistanceInputDTO inputDTO){
        Assistance assistance = convertFromDTO(inputDTO);
        Assistance saved = repository.save(assistance);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public AssistanceOutputDTO findById(Long id){
        Optional<Assistance> optional = repository.findById(id);
        Assistance assistance = optional.orElseThrow(()-> new AssistanceNotFoundException());
        return convertToDTO(assistance);
    }

    @Transactional(readOnly = true)
    public List<AssistanceOutputDTO> findBySpecialistId(Long specialistId){
        return specialistRepository.findById(specialistId).orElseThrow(()->new SpecialistNotFoundException())
                .getAssistances().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssistanceOutputDTO> findAll(){
        List<Assistance> all = repository.findAll();
        return all.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssistanceOutputDTO> findAll(Pageable pageable){
        return repository.findAll(pageable).get().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public AssistanceOutputDTO update(Long id, AssistanceInputDTO inputDTO){
        Optional<Assistance> optional = repository.findById(id);
        Assistance assistance = optional.orElseThrow(()-> new AssistanceNotFoundException());
        assistance.setTitle(inputDTO.getTitle());
        Assistance saved = repository.save(assistance);
        return convertToDTO(saved);
    }

    @Transactional
    public void removeById(Long assistanceId){
        repository.deleteById(assistanceId);
    }

    public Assistance convertFromDTO(AssistanceInputDTO inputDTO){
        return Assistance.builder()
                .title(inputDTO.getTitle())
                .build();
    }

    public AssistanceOutputDTO convertToDTO(Assistance input){
        return AssistanceOutputDTO.builder()
                .id(input.getId())
                .title(input.getTitle())
                .build();
    }
}
