package ir.maktab.finalproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ir.maktab.finalproject.controller.dto.ResponseTemplate;
import ir.maktab.finalproject.service.RequestService;
import ir.maktab.finalproject.service.UserService;
import ir.maktab.finalproject.service.dto.output.RequestOutputDTO;
import ir.maktab.finalproject.service.dto.output.UserOutputDTO;
import ir.maktab.finalproject.util.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
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

    @Autowired
    MailSender mailSender;

    @Autowired
    Captcha captchaUtil;

    @Value("${secret}")
    private String secret;

    private Algorithm algorithm ;

    @PostConstruct
    private void init(){
        algorithm = Algorithm.HMAC256(secret.getBytes());
    }

    @GetMapping("/getcaptcha")
    public void getCaptcha(HttpServletRequest request , HttpServletResponse response){
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Access-Control-Allow-Credentials" ,"true");
        response.setDateHeader("Max-Age", 0);
        response.setContentType("image/jpeg");
        String captchaStr= captchaUtil.generateCaptcha(6);
        try {
            int width=200;
            int height=80;
            Color bg = new Color(0,255,255);
            Color fg = new Color(0,100,0);
            Font font = new Font("Arial", Font.BOLD, 40);
            BufferedImage cpimg =new BufferedImage(width,height,BufferedImage.OPAQUE);
            Graphics g = cpimg.createGraphics();
            g.setFont(font);
            g.setColor(bg);
            g.fillRect(0, 0, width, height);
            g.setColor(fg);
            g.drawString(captchaStr,30,55);
            HttpSession session = request.getSession(true);
            session.setAttribute("CAPTCHA", captchaStr);
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(cpimg, "jpeg", outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ResponseTemplate<UserOutputDTO>> verifyMail(@RequestParam String param){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(param);
        String subject = decodedJWT.getSubject();
        String email = decodedJWT.getClaim("email").asString();
        UserOutputDTO user = userService.verify(Long.valueOf(subject), email);
        return ResponseEntity.ok(ResponseTemplate.<UserOutputDTO>builder()
                .code(200)
                .message("mail verified successfully.")
                .data(user)
                .build());
    }

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
