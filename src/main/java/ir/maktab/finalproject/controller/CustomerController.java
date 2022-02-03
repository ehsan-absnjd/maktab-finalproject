package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.controller.dto.CustomerRegisterParam;
import ir.maktab.finalproject.controller.dto.ResponseTemplate;
import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.service.CustomerService;
import ir.maktab.finalproject.service.RequestService;
import ir.maktab.finalproject.service.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.service.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.service.dto.output.RequestOutputDTO;
import ir.maktab.finalproject.util.CaptchaValidator;
import ir.maktab.finalproject.util.VerificationMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private RequestService requestService;

    @Autowired
    CaptchaValidator validator;

    @Autowired
    VerificationMailSender mailSender;

    @PostMapping()
    public ResponseEntity<ResponseTemplate<CustomerOutputDTO>> registerCustomer(@Valid @RequestBody CustomerRegisterParam input, HttpServletRequest request){
        validator.validate(request, input.getCaptcha());
        CustomerInputDTO inputDTO = convertFromParam(input);
        CustomerOutputDTO saved = customerService.save(inputDTO);
        ResponseTemplate<CustomerOutputDTO> result = ResponseTemplate.<CustomerOutputDTO>builder()
                .message("customer registered successfully.")
                .code(201)
                .data(saved)
                .build();
        mailSender.sendVerificationMail(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("#id==authentication.name or hasAuthority('can_get_customers')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<CustomerOutputDTO>> getCustomer(@PathVariable String id){
        CustomerOutputDTO byId = customerService.findById(Long.valueOf(id));
        return ResponseEntity.ok().body(ResponseTemplate.<CustomerOutputDTO>builder()
                .code(200)
                .message("ok")
                .data(byId)
                .build());
    }

    @PreAuthorize("#customerId==authentication.name or hasAuthority('can_get_customers')")
    @GetMapping("/{id}/requests")
    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> getCustomersRequests(@PathVariable("id") String customerId , @RequestParam String status, Pageable pageable){
        List<RequestOutputDTO> requestOutputDTOS = requestService.findByCustomerId(Long.valueOf(customerId), RequestStatus.valueOf(status) , pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<RequestOutputDTO>>builder().code(200).message("ok").data(requestOutputDTOS).build());
    }

    public CustomerInputDTO convertFromParam(CustomerRegisterParam input){
        return CustomerInputDTO.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .password(input.getPassword())
                .build();
    }
}
