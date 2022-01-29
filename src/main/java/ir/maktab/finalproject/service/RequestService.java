package ir.maktab.finalproject.service;

import ir.maktab.finalproject.service.dto.input.EvaluationInputDTO;
import ir.maktab.finalproject.service.dto.input.RequestInputDTO;
import ir.maktab.finalproject.service.dto.output.RequestOutputDTO;
import ir.maktab.finalproject.entity.*;
import ir.maktab.finalproject.exception.*;
import ir.maktab.finalproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RequestService {
    @Autowired
    private RequestRepository repository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private SpecialistRepository specialistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SubAssistanceRepository subAssistanceRepository;

    @Transactional
    public RequestOutputDTO save(RequestInputDTO inputDTO){
        Customer customer = customerRepository
                .findById(inputDTO.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException());
        SubAssistance subAssistance = subAssistanceRepository
                .findById(inputDTO.getSubAssistanceId()).orElseThrow(() -> new SubAssistanceNotFoundException());
        Request request = convertFromDTO(inputDTO);
        request.setCustomer(customer);
        request.setSubAssistance(subAssistance);
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public RequestOutputDTO findById(Long id){
        Request request = repository.findById(id).orElseThrow(() -> new RequestNotFoundException());
        return convertToDTO(request);
    }

    @Transactional(readOnly = true)
    public List<RequestOutputDTO> findAll(){
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequestOutputDTO> findAll(Pageable pageable){
        return repository.findAll(pageable).map(this::convertToDTO).get().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequestOutputDTO> findForSpecialist(Long specialistId, Pageable pageable){
        return repository.findForSpecialist(specialistId , pageable).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequestOutputDTO> findByCustomerId(Long customerId , RequestStatus status, Pageable pageable){
        return repository.findByCustomerId(customerId , status, pageable).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequestOutputDTO> findBySpecialistId(Long specialistId , RequestStatus status ,  Pageable pageable){
        return repository.findBySpecialistId(specialistId , status , pageable).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequestOutputDTO> findByUserId(Long userId , Pageable pageable){
        return repository.findByUserId(userId, pageable, RequestStatus.DONE , RequestStatus.PAID).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequestOutputDTO> findByParameterMap(Map<String, String> parameterMap){
        return repository.findByParameterMap(parameterMap).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public RequestOutputDTO update(Long id, RequestInputDTO inputDTO){
        Request request = repository.findById(id).orElseThrow(() -> new RequestNotFoundException());
        SubAssistance subAssistance = subAssistanceRepository
                .findById(inputDTO.getSubAssistanceId()).orElseThrow(() -> new SubAssistanceNotFoundException());
        request.setSubAssistance(subAssistance);
        request.setOfferedPrice(inputDTO.getOfferedPrice());
        request.setDescription(inputDTO.getDescription());
        request.setExecutionDate(inputDTO.getExecutionDate());
        request.setAddress(inputDTO.getAddress());
        return convertToDTO(request);
    }

    @Transactional
    public RequestOutputDTO selectOffer(Long requestId, Long offerId){
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new OfferNotFoundException());
        if(!Objects.equals(offer.getRequest().getId(), requestId))
            throw new InvalidOfferSelectionException();
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        request.setSelectedOffer(offer);
        request.setStatus(RequestStatus.WAITING_ARRIVAL);
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }



    @Transactional
    public RequestOutputDTO markBegun(Long requestId){
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        if (request.getStatus()!=RequestStatus.WAITING_ARRIVAL)
            throw new InvalidBeginRequestException();
        request.setStatus(RequestStatus.BEGUN);
        request.setBeginTime(new Date());
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }

    @Transactional
    public RequestOutputDTO markDone(Long requestId){
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        if (request.getStatus()!=RequestStatus.BEGUN)
            throw new InvalidFinishRequestException();
        request.setStatus(RequestStatus.DONE);
        request.setFinishTime(new Date());
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }

    @Transactional
    public RequestOutputDTO evaluate(Long requestId, EvaluationInputDTO inputDTO){
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        if ( request.getStatus()!= RequestStatus.DONE && request.getStatus()!= RequestStatus.PAID)
            throw new InvalidRequestEvaluationException();
        Offer selectedOffer = request.getSelectedOffer();
        Specialist specialist = selectedOffer.getSpecialist();
        request.setComment(inputDTO.getComment());
        Double promisedTime = selectedOffer.getExecutionPeriod();
        Date finishTime = request.getFinishTime();
        Date beginTime = request.getBeginTime();
        double executionTime = (finishTime.getTime() - beginTime.getTime())/3_600_000d;
        double points = inputDTO.getPoints();
        if (promisedTime<executionTime)
            points*=1-(executionTime-promisedTime)*.05;
        request.setPoints(points);
        Request saved = repository.save(request);
        specialistRepository.updateSpecialistPoints(specialist.getId());
        return convertToDTO(saved);
    }

    @Transactional
    public RequestOutputDTO pay(Long requestId){
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        Offer selectedOffer = request.getSelectedOffer();
        Specialist specialist = selectedOffer.getSpecialist();
        Customer customer = request.getCustomer();
        Double price = selectedOffer.getPrice();
        if (request.getStatus()!= RequestStatus.DONE )
            throw new InvalidRequestStatusException();
        if (customer.getCredit() < price)
            throw new RequestSettlementException("customer credit is not enough");
        specialist.setCredit(specialist.getCredit() + price*.7);
        customer.setCredit(customer.getCredit() - price);
        request.setStatus(RequestStatus.PAID);
        customerRepository.save(customer);
        specialistRepository.save(specialist);
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }

    @Transactional
    public void removeById(Long requestId){
        repository.deleteById(requestId);
    }

    public Request convertFromDTO(RequestInputDTO inputDTO){
        Request request =  Request.builder()
                .offeredPrice(inputDTO.getOfferedPrice())
                .description(inputDTO.getDescription())
                .executionDate(inputDTO.getExecutionDate())
                .address(inputDTO.getAddress())
                .status(RequestStatus.WAITING_FOR_OFFERS)
                .registerDate(new Date())
                .build();
        return request;
    }

    public RequestOutputDTO convertToDTO(Request input){
        return RequestOutputDTO.builder()
                .id(input.getId())
                .customerId(input.getCustomer().getId())
                .subAssistanceId(input.getSubAssistance().getId())
                .offeredPrice(input.getOfferedPrice())
                .description(input.getDescription())
                .registerDate(input.getRegisterDate())
                .executionDate(input.getExecutionDate())
                .address(input.getAddress())
                .status(input.getStatus())
                .selectedOffer(input.getSelectedOffer()!=null?input.getSelectedOffer().getId():null)
                .points(input.getPoints())
                .comment(input.getComment())
                .build();
    }
}