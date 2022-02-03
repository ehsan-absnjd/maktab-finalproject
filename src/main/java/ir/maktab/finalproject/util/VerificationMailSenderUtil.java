package ir.maktab.finalproject.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ir.maktab.finalproject.service.dto.output.UserOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class VerificationMailSenderUtil implements VerificationMailSender{
    @Value("${secret}")
    private String secret;

    @Value("${verificationMailExpirePeriod}")
    private String verificationMailExpirePeriod;

    @Autowired
    private MailSender mailSender;

    private Algorithm algorithm ;

    @PostConstruct
    private void init(){
        algorithm = Algorithm.HMAC256(secret.getBytes());
    }

    public void sendVerificationMail(UserOutputDTO userOutputDTO){
        String param = JWT.create()
                .withSubject(String.valueOf(userOutputDTO.getId()))
                .withClaim("email", userOutputDTO.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(verificationMailExpirePeriod) * 24 * 60 * 60 * 1000))
                .sign(algorithm);
        String from = "maktab.serviceprovider@gmail.com";
        String to = userOutputDTO.getEmail();
        SimpleMailMessage message = new SimpleMailMessage();
        UriComponents contextPath = ServletUriComponentsBuilder.fromCurrentContextPath().build();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Maktab service provider email verification");
        message.setText("verify your mail by clicking on this link: \n"+contextPath+"/users/verify?param=" +param);
        mailSender.send(message);
    }
}
