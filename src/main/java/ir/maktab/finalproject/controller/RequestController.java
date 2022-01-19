package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.controller.dto.*;
import ir.maktab.finalproject.exception.IllegalParameterException;
import ir.maktab.finalproject.exception.UnauthorizedCustomerException;
import ir.maktab.finalproject.service.OfferService;
import ir.maktab.finalproject.service.RequestService;
import ir.maktab.finalproject.service.dto.input.EvaluationInputDTO;
import ir.maktab.finalproject.service.dto.input.OfferInputDTO;
import ir.maktab.finalproject.service.dto.input.RequestInputDTO;
import ir.maktab.finalproject.service.dto.output.OfferOutputDTO;
import ir.maktab.finalproject.service.dto.output.RequestOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requests")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @Autowired
    private OfferService offerService;

    @PreAuthorize("hasAuthority('can_add_request')")
    @PostMapping
    public ResponseEntity<ResponseTemplate<RequestOutputDTO>> addRequest(@Valid RequestInputParam inputParam){
        RequestInputDTO dto = convertFromParam(inputParam);
        RequestOutputDTO saved = requestService.save(dto);
        ResponseTemplate<RequestOutputDTO> result = ResponseTemplate.<RequestOutputDTO>builder()
                .message("request registered successfully.")
                .code(201)
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAuthority('can_get_requests_by_parameter')")
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> getRequests(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> parameters = new HashMap<>();
        parameterMap.entrySet().forEach(entry-> parameters.put(entry.getKey(), entry.getValue()[0]));
        List<RequestOutputDTO> requests = requestService.findByParameterMap(parameters);
        return ResponseEntity.ok().body(ResponseTemplate.<List<RequestOutputDTO>>builder()
                        .data(requests)
                        .code(200)
                        .message("ok")
                .build());
    }

    @PreAuthorize("hasAuthority('can_evaluate')")
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<ResponseTemplate<RequestOutputDTO>> addEvaluation(@PathVariable("id") Long requestId , EvaluationInputParam inputParam){
        authorize(requestId);
        EvaluationInputDTO dto = EvaluationInputDTO.builder().comment(inputParam.getComment()).points(inputParam.getPoints()).build();
        RequestOutputDTO requestOutputDTO = requestService.evaluate(requestId, dto);
        return ResponseEntity.ok().body(ResponseTemplate.<RequestOutputDTO>builder()
                        .message("evaluation added successfully")
                        .data(requestOutputDTO)
                        .code(200)
                .build());
    }

    @PreAuthorize("hasAuthority('can_add_offers')")
    @PostMapping("/{id}/offers")
    public ResponseEntity<ResponseTemplate<OfferOutputDTO>> addOfferToRequest(@PathVariable("id") Long requestId , OfferInputParam inputParam ){
        OfferInputDTO dto = convertFromParam(inputParam);
        OfferOutputDTO saved = offerService.save(requestId, dto);
        ResponseTemplate<OfferOutputDTO> result = ResponseTemplate.<OfferOutputDTO>builder()
                .message("request registered successfully.")
                .code(201)
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAuthority('can_get_offers')")
    @GetMapping("/{id}/offers")
    public ResponseEntity<ResponseTemplate<List<OfferOutputDTO>>> getOffers(@PathVariable("id") Long requestId, @RequestParam String orderby , Pageable pageable){
        authorize(requestId);
        List<OfferOutputDTO> offers;
        switch (orderby){
            case "pointdesc":
                offers = offerService.findByRequestIdOrderByPointsDesc(requestId, pageable);
                break;
            case "ss":
                offers = offerService.findByRequestIdOrderByPriceAsc(requestId , pageable);
                break;
            default:
                throw new IllegalParameterException();
        }
        return ResponseEntity.ok(ResponseTemplate.<List<OfferOutputDTO>>builder()
                        .data(offers)
                        .code(200)
                        .message("ok")
                .build());
    }

    @PreAuthorize("hasAuthority('can_select_offer')")
    @PostMapping("/{id}/selectoffer")
    public ResponseEntity<ResponseTemplate<RequestOutputDTO>> selectOffer(@PathVariable("id") Long requestId , SelectOfferParam param ){

        RequestOutputDTO requestOutputDTO = requestService.selectOffer(requestId, param.getOfferId());
        return ResponseEntity.ok(ResponseTemplate.<RequestOutputDTO>builder()
                .code(201)
                .message("offer selected successfully")
                .data(requestOutputDTO)
                .build());
    }

    @PreAuthorize("hasAuthority('can_pay_request')")
    @PostMapping("/{id}/pay")
    public void payRequest(@PathVariable("id") Long requestId ){
        authorize(requestId);
        requestService.pay(requestId);
    }

    private void authorize(Long requestId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long customerId = Long.valueOf(authentication.getName());
        RequestOutputDTO request = requestService.findById(requestId);
        if (!request.getCustomerId().equals(customerId))
            throw new UnauthorizedCustomerException();
    }

    private OfferInputDTO convertFromParam(OfferInputParam inputParam) {
        return OfferInputDTO.builder()
                .beginning(inputParam.getBeginning())
                .executionPeriod(inputParam.getExecutionPeriod())
                .price(inputParam.getPrice())
                .specialistId(inputParam.getSpecialistId())
                .build();
    }

    private RequestInputDTO convertFromParam(RequestInputParam inputParam) {
        return RequestInputDTO.builder()
                .customerId(inputParam.getCustomerId())
                .subAssistanceId(inputParam.getSubAssistanceId())
                .address(inputParam.getAddress())
                .offeredPrice(inputParam.getOfferedPrice())
                .description(inputParam.getDescription())
                .executionDate(inputParam.getExecutionDate())
                .build();
    }
}
