package ir.maktab.finalproject.service;

import ir.maktab.finalproject.dto.input.OfferInputDTO;
import ir.maktab.finalproject.dto.output.OfferOutputDTO;
import ir.maktab.finalproject.entity.Offer;
import ir.maktab.finalproject.entity.Request;
import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.entity.Specialist;
import ir.maktab.finalproject.exception.InvalidOfferRegisterException;
import ir.maktab.finalproject.exception.LowerThanBasePriceException;
import ir.maktab.finalproject.exception.RequestNotFoundException;
import ir.maktab.finalproject.exception.SpecialistNotFoundException;
import ir.maktab.finalproject.repository.OfferRepository;
import ir.maktab.finalproject.repository.RequestRepository;
import ir.maktab.finalproject.repository.SpecialistRepository;
import ir.maktab.finalproject.repository.SubAssistanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    @Autowired
    OfferRepository repository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    SpecialistRepository specialistRepository;

    @Autowired
    SubAssistanceRepository subAssistanceRepository;

    @Transactional
    public OfferOutputDTO save(Long requestId , OfferInputDTO inputDTO){
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        Offer offer = convertFromDTO(inputDTO);
        if (offer.getPrice()<request.getSubAssistance().getBasePrice())
            throw new LowerThanBasePriceException();
        if(request.getStatus()!= RequestStatus.WAITING_FOR_OFFERS && request.getStatus() != RequestStatus.WAITING_FOR_SELECT)
            throw new InvalidOfferRegisterException();
        offer.setRequest(request);
        request.setStatus(RequestStatus.WAITING_FOR_SELECT);
        Offer saved = repository.save(offer);
        requestRepository.save(request);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public OfferOutputDTO findByRequestIdAndOfferId(Long requestId , Long offerId){
        Offer offer = repository.findByRequestIdAndOfferId(requestId, offerId);
        return convertToDTO(offer);
    }

    @Transactional(readOnly = true)
    public List<OfferOutputDTO> findByRequestId(Long requestId ){
        List<Offer> offers = repository.findByRequestId(requestId);
        return offers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OfferOutputDTO> findByRequestId(Long requestId , Pageable pageable){
         return repository.findByRequestId(requestId, pageable).stream()
            .map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OfferOutputDTO> findByRequestIdOrderByPointsDesc(Long requestId , Pageable pageable){
        return repository.findByRequestIdOrderByPointsDesc(requestId, pageable).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OfferOutputDTO> findByRequestIdOrderByPriceAsc(Long requestId , Pageable pageable){
        return repository.findByRequestIdOrderByPriceAsc(requestId, pageable).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OfferOutputDTO update(Long requestId , Long offerId , OfferInputDTO inputDTO){
        Offer offer = repository.findByRequestIdAndOfferId(requestId, offerId);
        Specialist specialist = specialistRepository.findById(inputDTO.getSpecialistId())
                .orElseThrow(() -> new SpecialistNotFoundException());
        offer.setSpecialist(specialist);
        offer.setPrice(inputDTO.getPrice());
        offer.setExecutionPeriod(inputDTO.getExecutionPeriod());
        offer.setBeginning(inputDTO.getBeginning());
        Offer saved = repository.save(offer);
        return convertToDTO(saved);
    }


    private OfferOutputDTO convertToDTO(Offer input) {
        return OfferOutputDTO.builder()
                .id(input.getId())
                .specialistId(input.getSpecialist().getId())
                .registerDate(input.getRegisterDate())
                .price(input.getPrice())
                .executionPeriod(input.getExecutionPeriod())
                .beginning(input.getBeginning())
                .requestId(input.getRequest().getId())
                .build();
    }

    public Offer convertFromDTO(OfferInputDTO inputDTO){
        Specialist specialist = specialistRepository.findById(inputDTO.getSpecialistId()).orElseThrow(SpecialistNotFoundException::new);
        Offer offer = Offer.builder()
                .beginning(inputDTO.getBeginning())
                .executionPeriod(inputDTO.getExecutionPeriod())
                .registerDate(new Date())
                .price(inputDTO.getPrice())
                .build();
        offer.setSpecialist(specialist);
        return offer;
    }


}
