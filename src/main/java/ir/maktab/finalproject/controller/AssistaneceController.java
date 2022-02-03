package ir.maktab.finalproject.controller;


import ir.maktab.finalproject.controller.dto.AssistanceInputParam;
import ir.maktab.finalproject.controller.dto.ResponseTemplate;
import ir.maktab.finalproject.controller.dto.SubAssistanceInputParam;
import ir.maktab.finalproject.service.AssistanceService;
import ir.maktab.finalproject.service.SubAssistanceService;
import ir.maktab.finalproject.service.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.service.dto.input.SubAssistanceInputDTO;
import ir.maktab.finalproject.service.dto.output.AssistanceOutputDTO;
import ir.maktab.finalproject.service.dto.output.SubAssistanceOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/assistances")
public class AssistaneceController {
    @Autowired
    private AssistanceService assistanceService;

    @Autowired
    private SubAssistanceService subAssistanceService;

    @PreAuthorize("hasAuthority('can_add_assistance')")
    @PostMapping
    public ResponseEntity<ResponseTemplate<AssistanceOutputDTO>> addAssistance(@Valid @RequestBody AssistanceInputParam inputParam){
        AssistanceInputDTO dto = AssistanceInputDTO.builder().title(inputParam.getTitle()).build();
        AssistanceOutputDTO saved = assistanceService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.<AssistanceOutputDTO>builder()
                        .code(201)
                        .message("assistance created successfully.")
                        .data(saved).build());
    }

    @GetMapping
    public ResponseEntity<ResponseTemplate<List<AssistanceOutputDTO>>> getAssistances(){
        List<AssistanceOutputDTO> retrieved = assistanceService.findAll();
        return ResponseEntity.ok(ResponseTemplate.<List<AssistanceOutputDTO>>builder()
                .code(200)
                .message("ok")
                .data(retrieved)
                .build());
    }

    @PreAuthorize("hasAuthority('can_add_subassistance')")
    @PostMapping("/{id}/subassistances")
    public ResponseEntity<ResponseTemplate<SubAssistanceOutputDTO>> addSubAssistance
            (@PathVariable Long id, @Valid @RequestBody SubAssistanceInputParam inputParam){
        SubAssistanceInputDTO dto = SubAssistanceInputDTO.builder()
                .basePrice(inputParam.getBasePrice())
                .description(inputParam.getDescription())
                .title(inputParam.getTitle()).build();
        SubAssistanceOutputDTO saved = subAssistanceService.save(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.<SubAssistanceOutputDTO>builder()
                        .code(201)
                        .message("subassistance created successfully.")
                        .data(saved).build());
    }

    @GetMapping("/{id}/subassistances")
    public ResponseEntity<ResponseTemplate<List<SubAssistanceOutputDTO>>> getSubAssistances(@PathVariable(name = "id") Long assistanceId ){
        List<SubAssistanceOutputDTO> retrieved = subAssistanceService.findAll(assistanceId );
        return ResponseEntity.ok(ResponseTemplate.<List<SubAssistanceOutputDTO>>builder()
                .code(200)
                .message("ok")
                .data(retrieved)
                .build());
    }
}
