package ir.maktab.finalproject.service;

import ir.maktab.finalproject.dto.input.EvaluationInputDTO;
import ir.maktab.finalproject.dto.input.RequestInputDTO;
import ir.maktab.finalproject.dto.output.RequestOutputDTO;
import ir.maktab.finalproject.entity.*;
import ir.maktab.finalproject.exception.*;
import ir.maktab.finalproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    RequestRepository repository;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    SpecialistRepository specialistRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SubAssistanceRepository subAssistanceRepository;

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
        if(offer.getRequest().getId()!=requestId)
            throw new InvalidOfferSelectionException();
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        if(request.getCustomer().getCredit()<offer.getPrice())
            throw new NotEnoughCreditException();
        request.setSelectedOffer(offer);
        request.setStatus(RequestStatus.WAITING_ARRIVAL);
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }

    @Transactional
    public RequestOutputDTO unSelectOffer(Long requestId){
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        if(request.getStatus()!=RequestStatus.WAITING_ARRIVAL)
            throw new InvalidCanselException();
        request.setStatus(RequestStatus.WAITING_FOR_SELECT);
        request.setSelectedOffer(null);
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }

    @Transactional
    public RequestOutputDTO markBegun(Long requestId){
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        if (request.getStatus()!=RequestStatus.WAITING_ARRIVAL)
            throw new InvalidBeginRequestException();
        request.setStatus(RequestStatus.BEGUN);
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }

    @Transactional
    public RequestOutputDTO markDone(Long requestId){
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        if (request.getStatus()!=RequestStatus.BEGUN)
            throw new InvalidFinishRequestException();
        request.setStatus(RequestStatus.DONE);
        Request saved = repository.save(request);
        return convertToDTO(saved);
    }

    @Transactional
    public RequestOutputDTO settle(Long requestId, EvaluationInputDTO inputDTO){
        Request request = repository.findById(requestId).orElseThrow(() -> new RequestNotFoundException());
        Offer selectedOffer = request.getSelectedOffer();
        Specialist specialist = selectedOffer.getSpecialist();
        Customer customer = request.getCustomer();
        Double price = selectedOffer.getPrice();
        if (request.getStatus()!= RequestStatus.DONE )
            throw new InvalidPayRequestException();
        if (customer.getCredit() < price)
            throw new RequestSettlementException("customer credit is not enough");
        request.setComment(inputDTO.getComment());
        request.setPoints(inputDTO.getPoints());
        specialist.setCredit(specialist.getCredit() + price);
        customer.setCredit(customer.getCredit() - price);
        request.setStatus(RequestStatus.PAID);
        customerRepository.save(customer);
        Request saved = repository.save(request);
        specialistRepository.save(specialist);
        specialistRepository.updateSpecialistPoints(specialist.getId());
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
