package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.controller.dto.AddAssistanceInputParam;
import ir.maktab.finalproject.controller.dto.CustomerRegisterParam;
import ir.maktab.finalproject.controller.dto.ResponseTemplate;
import ir.maktab.finalproject.controller.dto.SpecialistRegisterParam;
import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.service.CustomerService;
import ir.maktab.finalproject.service.RequestService;
import ir.maktab.finalproject.service.SpecialistService;
import ir.maktab.finalproject.service.UserService;
import ir.maktab.finalproject.service.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.service.dto.input.SpecialistInputDTO;
import ir.maktab.finalproject.service.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.service.dto.output.RequestOutputDTO;
import ir.maktab.finalproject.service.dto.output.SpecialistOutputDTO;
import ir.maktab.finalproject.service.dto.output.UserOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Value("${uploaddir}")
    private String uploadDir;

    private Path fileStorageLocation;

    @PostConstruct
    public void init(){
        fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("could not create directory.");
        }
    }

    @GetMapping("/test")
    @PreAuthorize("#id == authentication.name")
    public ResponseEntity<String> testValid(@RequestParam Long id){
        return ResponseEntity.ok("ok"+id);
    }

    @PostMapping("/customers")
    public ResponseEntity<ResponseTemplate<CustomerOutputDTO>> registerCustomer(@Valid @RequestBody CustomerRegisterParam input){
        CustomerInputDTO inputDTO = convertFromParam(input);
        CustomerOutputDTO saved = customerService.save(inputDTO);
        ResponseTemplate<CustomerOutputDTO> result = ResponseTemplate.<CustomerOutputDTO>builder()
                .message("customer registered successfully.")
                .code(201)
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

//    @GetMapping("/customers/{id}/requests")
//    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> getCustomersRequests(@PathVariable String customerId , @RequestParam String status, Pageable pageable){
//        List<RequestOutputDTO> requestOutputDTOS = requestService.findByCustomerId(Long.valueOf(customerId), RequestStatus.valueOf(status) , pageable);
//        return ResponseEntity.ok(ResponseTemplate.<List<RequestOutputDTO>>builder().code(200).message("ok").data(requestOutputDTOS).build());
//    }

    @PostMapping("/specialists")
    public ResponseEntity<ResponseTemplate<SpecialistOutputDTO>> registerSpecialist(@Valid @RequestBody SpecialistRegisterParam input){
        SpecialistInputDTO inputDTO = convertFromParam(input);
        SpecialistOutputDTO saved = specialistService.save(inputDTO);
        ResponseTemplate<SpecialistOutputDTO> result = ResponseTemplate.<SpecialistOutputDTO>builder()
                .message("specialist registered successfully.")
                .code(201)
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("#specialistId==authentication.name or hasAuthority('canaddassistance')")
    @PostMapping("/specialists/{id}/assistances")
    public ResponseEntity<ResponseTemplate<Object>> addAssistanceToSpecialist(@PathVariable(name = "id") String specialistId , @Valid AddAssistanceInputParam inputParam){
        specialistService.addAssistance(Long.valueOf(specialistId) , inputParam.getAssistanceId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.builder().code(201).message("assistance added successfully.").build());
    }

    @GetMapping("/specialists/{id}/relevantrequests")
    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> getRevelentRequests(@PathVariable String specialistId, Pageable pageable){
        List<RequestOutputDTO> requestOutputDTOS = requestService.findForSpecialist(Long.valueOf(specialistId), pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<RequestOutputDTO>>builder().code(200).message("ok").data(requestOutputDTOS).build());
    }
//
//    @GetMapping("/specialists/{id}/requests")
//    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> getSpecialistRequests(@PathVariable String specialistId, @RequestParam String status,  Pageable pageable){
//        List<RequestOutputDTO> requestOutputDTOS = requestService.findBySpecialistId(Long.valueOf(specialistId), RequestStatus.valueOf(status), pageable);
//        return ResponseEntity.ok(ResponseTemplate.<List<RequestOutputDTO>>builder().code(200).message("ok").data(requestOutputDTOS).build());
//    }

    @PostMapping("/uploadfile")
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getName();
        System.out.println(email);
        Path targetLocation = this.fileStorageLocation.resolve(email+postfix);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("exception");
            e.printStackTrace();
        }
    }

//    @PreAuthorize("hasAuthority('view')")
//    @GetMapping("/users")
//    public ResponseEntity<ResponseTemplate<List<UserOutputDTO>>> findUserByParam(HttpServletRequest request , Pageable pageable){
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        List<UserOutputDTO> userOutputDTOList = userService.findByParameters(parameterMap);
//        return ResponseEntity.ok(ResponseTemplate.<List<UserOutputDTO>>builder()
//                        .data(userOutputDTOList)
//                        .message("ok")
//                        .code(200)
//                .build());
//    }

    @PreAuthorize("hasAuthority('view')")
    @GetMapping("/users/{id}/requests")
    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> findUserRequests(@PathVariable String id, Pageable pageable){
        List<RequestOutputDTO> requestOutputDTOList=null;
        UserOutputDTO user = userService.findById(Long.valueOf(id));
//        if (user instanceof CustomerOutputDTO) {
//            requestOutputDTOList = requestService.findByCustomerId(user.getId(), pageable);
//        }else {
//            requestOutputDTOList = requestService.findBySpecialistId(user.getId(), pageable);
//        }
        return ResponseEntity.ok(ResponseTemplate.<List<RequestOutputDTO>>builder()
                        .code(200)
                        .message("ok")
                        .data(requestOutputDTOList)
                .build());
    }

    private SpecialistInputDTO convertFromParam(SpecialistRegisterParam input) {
        return SpecialistInputDTO.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getLastName())
                .password(input.getPassword())
                .credit(input.getCredit())
                .build();
    }

    public CustomerInputDTO convertFromParam(CustomerRegisterParam input){
        return CustomerInputDTO.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .password(input.getPassword())
                .credit(input.getCredit())
                .build();
    }
}
