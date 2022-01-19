package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.controller.dto.CustomerRegisterParam;
import ir.maktab.finalproject.controller.dto.ResponseTemplate;
import ir.maktab.finalproject.service.RequestService;
import ir.maktab.finalproject.service.UserService;
import ir.maktab.finalproject.service.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.service.dto.output.RequestOutputDTO;
import ir.maktab.finalproject.service.dto.output.UserOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @PreAuthorize("hasAuthority('can_get_user_requests')")
    @GetMapping("/{id}/requests")
    public ResponseEntity<ResponseTemplate<List<RequestOutputDTO>>> findUserRequests(@PathVariable Long id, Pageable pageable){
        List<RequestOutputDTO> userRequests = requestService.findByUserId(id, pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<RequestOutputDTO>>builder()
                        .code(200)
                        .message("ok")
                        .data(userRequests)
                .build());
    }

    @PreAuthorize("hasAuthority('can_get_users')")
    @GetMapping()
    public ResponseEntity<ResponseTemplate<List<UserOutputDTO>>> findUserByParam(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> parameters = new HashMap<>();
        parameterMap.entrySet().forEach(entry-> parameters.put(entry.getKey(), entry.getValue()[0]));
        List<UserOutputDTO> userOutputDTOList = userService.findByParameters(parameters);
        return ResponseEntity.ok(ResponseTemplate.<List<UserOutputDTO>>builder()
                        .data(userOutputDTOList)
                        .message("ok")
                        .code(200)
                .build());
    }

    @PreAuthorize("hasAuthority('can_get_report')")
    @GetMapping("/report")
    public ResponseEntity<ResponseTemplate<List<UserOutputDTO>>> getReport(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> parameters = new HashMap<>();
        parameterMap.entrySet().forEach(entry-> parameters.put(entry.getKey(), entry.getValue()[0]));
        List<UserOutputDTO> report = userService.getReportByParameters(parameters);
        return ResponseEntity.ok(ResponseTemplate.<List<UserOutputDTO>>builder()
                .data(report)
                .message("ok")
                .code(200)
                .build());
    }
}
