package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.controller.dto.AddAssistanceInputParam;
import ir.maktab.finalproject.controller.dto.ResponseTemplate;
import ir.maktab.finalproject.controller.dto.SpecialistRegisterParam;
import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.exception.InvalidFileFormatException;
import ir.maktab.finalproject.exception.FileNotFoundException;
import ir.maktab.finalproject.exception.UnauthenticatedException;
import ir.maktab.finalproject.service.RequestService;
import ir.maktab.finalproject.service.SpecialistService;
import ir.maktab.finalproject.service.dto.input.SpecialistInputDTO;
import ir.maktab.finalproject.service.dto.output.RequestOutputDTO;
import ir.maktab.finalproject.service.dto.output.SpecialistOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/specialists")
public class SpecialistController {
    @Autowired
    private SpecialistService specialistService;

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

    @PostMapping()
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

    @PreAuthorize("#id==authentication.name or hasAuthority('can_get_specialists')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<SpecialistOutputDTO>> getSpecialist(@PathVariable String id){
        SpecialistOutputDTO byId = specialistService.findById(Long.valueOf(id));
        return ResponseEntity.ok().body(ResponseTemplate.<SpecialistOutputDTO>builder()
                        .code(200)
                        .message("ok")
                        .data(byId)
                .build());
    }

    @PreAuthorize("#specialistId==authentication.name or hasAuthority('can_assign_assistance')")
    @PostMapping("/{id}/assistances")
    public ResponseEntity<ResponseTemplate<Object>> addAssistanceToSpecialist(@PathVariable(name = "id") String specialistId , @Valid AddAssistanceInputParam inputParam){
        specialistService.addAssistance(Long.valueOf(specialistId) , inputParam.getAssistanceId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.builder().code(201).message("assistance added successfully.").build());
    }

    @PreAuthorize("#specialistId==authentication.name")
    @GetMapping("/{id}/relevantrequests")
    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> getRevelentRequests(@PathVariable String specialistId, Pageable pageable){
        List<RequestOutputDTO> requestOutputDTOS = requestService.findForSpecialist(Long.valueOf(specialistId), pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<RequestOutputDTO>>builder().code(200).message("ok").data(requestOutputDTOS).build());
    }

    @PreAuthorize("#specialistId==authentication.name or hasAuthority('can_get_specialists')")
    @GetMapping("/{id}/requests")
    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> getSpecialistRequests(@PathVariable String specialistId, @RequestParam String status,  Pageable pageable){
        List<RequestOutputDTO> requestOutputDTOS = requestService.findBySpecialistId(Long.valueOf(specialistId), RequestStatus.valueOf(status), pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<RequestOutputDTO>>builder().code(200).message("ok").data(requestOutputDTOS).build());
    }

    @PostMapping("/photos")
    public ResponseEntity<ResponseTemplate<String>> uploadPhoto(@RequestParam("file") MultipartFile file) {
        List<String> allowedTypes = Arrays.asList(".png" , ".jpg" );
        String originalFilename = file.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!allowedTypes.contains(postfix.toLowerCase()))
            throw new InvalidFileFormatException();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser" ))
            throw new UnauthenticatedException();
        Long id = Long.valueOf( authentication.getName());
        SpecialistOutputDTO specialist = specialistService.findById(id);
        if (specialist.getPhotoURL()!=null){
            removeFile(specialist.getPhotoURL());
        }
        Path targetLocation = Paths.get(uploadDir+authentication.getName()+postfix);
        System.out.println("created : " + targetLocation.toString());
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString()
                +"/specialists/photos/"+authentication.getName()+postfix;
        System.out.println(url);
        specialistService.changePhotoUrl(id, url);
        return ResponseEntity.created(URI.create(url)).body(ResponseTemplate.<String>builder()
                .code(201).message("photo uploaded successfully.").data(url).build());
    }

    @GetMapping("/photos/{file}")
    public ResponseEntity<Resource> getPhoto(@PathVariable("file") String file) {
        try {
            Path filePath = Paths.get(uploadDir +file);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new FileNotFoundException();
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException();
        }
    }

    private void removeFile(String photoURL) {
        String fileName = photoURL.substring(photoURL.lastIndexOf('/'));
        Path path = Paths.get(uploadDir+fileName);
        System.out.println("deleted : " + path.toString());
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
