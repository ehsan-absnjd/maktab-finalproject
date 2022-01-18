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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class AssistaneceController {
    @Autowired
    private AssistanceService assistanceService;

    @Autowired
    private SubAssistanceService subAssistanceService;

    @PostMapping("/assistances")
    public ResponseEntity<ResponseTemplate<AssistanceOutputDTO>> addAssistance(@Valid AssistanceInputParam inputParam){
        AssistanceInputDTO dto = AssistanceInputDTO.builder().title(inputParam.getTitle()).build();
        AssistanceOutputDTO saved = assistanceService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.<AssistanceOutputDTO>builder()
                        .code(201)
                        .message("assistance created successfully.")
                        .data(saved).build());
    }

    @GetMapping("/assistances")
    public ResponseEntity<ResponseTemplate<List<AssistanceOutputDTO>>> getAssistances(Pageable pageable){
        List<AssistanceOutputDTO> retrieved = assistanceService.findAll(pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<AssistanceOutputDTO>>builder()
                .code(200)
                .message("ok")
                .data(retrieved)
                .build());
    }

    @PostMapping("/assistances/{id}/subassistances")
    public ResponseEntity<ResponseTemplate<SubAssistanceOutputDTO>> addSubAssistance
            (@PathVariable(name = "id") Long assistanceId, @Valid SubAssistanceInputParam inputParam){
        SubAssistanceInputDTO dto = SubAssistanceInputDTO.builder()
                .basePrice(inputParam.getBasePrice())
                .description(inputParam.getDescription())
                .title(inputParam.getTitle()).build();
        SubAssistanceOutputDTO saved = subAssistanceService.save(assistanceId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.<SubAssistanceOutputDTO>builder()
                        .code(201)
                        .message("subassistance created successfully.")
                        .data(saved).build());
    }

    @GetMapping("/assistances/{id}/subassistances")
    public ResponseEntity<ResponseTemplate<List<SubAssistanceOutputDTO>>> getSubAssistances(@PathVariable(name = "id") Long assistanceId , Pageable pageable){
        List<SubAssistanceOutputDTO> retrieved = subAssistanceService.findAll(assistanceId, pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<SubAssistanceOutputDTO>>builder()
                .code(200)
                .message("ok")
                .data(retrieved)
                .build());
    }
}
