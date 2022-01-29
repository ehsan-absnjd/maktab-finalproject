package ir.maktab.finalproject;

import ir.maktab.finalproject.util.Captcha;
import ir.maktab.finalproject.util.CaptchaValidator;
import ir.maktab.finalproject.util.RequestAuthorizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@TestConfiguration
public class RestTestConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        User customer = new User("1", "password1", getCustomerAuthorities());
        User specialist = new User("2", "password2", getSpecialistAuthorities());
        User admin = new User("3", "password3", getAdminAuthorities());
        return new InMemoryUserDetailsManager(Arrays.asList(
                customer,specialist,admin
        ));
    }
    private List<GrantedAuthority> getAdminAuthorities() {
        String[] authorities = {"can_get_customers" , "can_get_specialists" ,"can_assign_assistance" ,
                "can_get_user_requests" , "can_get_users","can_add_assistance" , "can_add_subassistance" ,
                "can_get_report" , "can_get_requests_by_parameter",

        };
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getSpecialistAuthorities() {
        String[] authorities = {"can_add_offers"  };
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getCustomerAuthorities() {
        String[] authorities = {"can_add_request" , "can_evaluate" , "can_get_offers" , "can_select_offer" ,
                "can_pay_request"};
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    @Bean
    public Captcha captcha(){
        return new Captcha() {
            @Override
            public String generateCaptcha(int captchaLength) {
                return "";
            }
        };
    }

    @Bean
    public CaptchaValidator captchaValidator(){
        return new CaptchaValidator() {
            @Override
            public void validate(HttpServletRequest request) {
            }
        };
    }

    @Bean
    public RequestAuthorizer requestAuthorizer(){
        return new RequestAuthorizer() {
            @Override
            public void authorize(Long requestId) {
            }
        };
    }

    @Bean
    public MailSender mailSender(){
        return new MailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
            }
            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
