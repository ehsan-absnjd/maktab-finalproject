package ir.maktab.finalproject.service;

import ir.maktab.finalproject.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.dto.input.SubAssistanceInputDTO;
import ir.maktab.finalproject.dto.output.SubAssistanceOutputDTO;
import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.SubAssistance;
import ir.maktab.finalproject.exception.AssistanceNotFoundException;
import ir.maktab.finalproject.repository.AssistanceRepository;
import ir.maktab.finalproject.repository.SubAssistanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubAssistanceService{

    @Autowired
    SubAssistanceRepository repository;

    @Autowired
    AssistanceRepository assistanceRepository;

    @Transactional
    public SubAssistanceOutputDTO save(Long assistanceId , SubAssistanceInputDTO inputDTO){
        Optional<Assistance> optional = assistanceRepository.findById(assistanceId);
        Assistance assistance = optional.orElseThrow(() -> new AssistanceNotFoundException());
        SubAssistance subAssistance = convertFromDTO(inputDTO);
        assistance.addSubAssistance(subAssistance);
        assistanceRepository.save(assistance);
        return convertToDTO(subAssistance);
    }

    @Transactional(readOnly = true)
    public SubAssistanceOutputDTO findById(Long assistanceId , Long subAssistanceId){
        SubAssistance subAssistance = repository
                .findByAssistanceIdAndSubAssistanceId(assistanceId, subAssistanceId);
        return convertToDTO(subAssistance);
    }

    @Transactional(readOnly = true)
    public List<SubAssistanceOutputDTO> findAll(Long assistanceId ){
        List<SubAssistance> subAssistances =  repository.findByAssistanceId(assistanceId);
        return subAssistances.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubAssistanceOutputDTO> findAll(Long assistanceId , Pageable pageable){
        Page<SubAssistance> subAssistances = repository.findByAssistanceId(assistanceId , pageable);
        return subAssistances.get().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public SubAssistanceOutputDTO update(Long assistanceId , Long subAssistanceId , SubAssistanceInputDTO inputDTO){
        SubAssistance subAssistance =  repository
                .findByAssistanceIdAndSubAssistanceId(assistanceId, subAssistanceId);
        subAssistance.setTitle(inputDTO.getTitle());
        subAssistance.setDescription(inputDTO.getDescription());
        subAssistance.setBasePrice(inputDTO.getBasePrice());
        SubAssistance saved = repository.save(subAssistance);
        return convertToDTO(saved);
    }

    public SubAssistance convertFromDTO(SubAssistanceInputDTO inputDTO){
        return SubAssistance.builder()
                .title(inputDTO.getTitle())
                .description(inputDTO.getDescription())
                .basePrice(inputDTO.getBasePrice())
                .build();
    }

    public SubAssistanceOutputDTO convertToDTO(SubAssistance input){
        return SubAssistanceOutputDTO.builder()
                .id(input.getId())
                .title(input.getTitle())
                .description(input.getDescription())
                .basePrice(input.getBasePrice())
                .assistanceId(input.getAssistance().getId())
                .build();
    }
}
