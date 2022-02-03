package ir.maktab.finalproject;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.User;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.repository.UserRepository;
import ir.maktab.finalproject.service.AssistanceService;
import ir.maktab.finalproject.service.SpecialistService;
import ir.maktab.finalproject.service.UserService;
import ir.maktab.finalproject.service.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.service.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.service.CustomerService;
import ir.maktab.finalproject.service.dto.input.SpecialistInputDTO;
import ir.maktab.finalproject.service.dto.output.AssistanceOutputDTO;
import ir.maktab.finalproject.service.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.service.dto.output.SpecialistOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Date;
import java.util.Properties;

@SpringBootApplication
public class FinalProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }

    @Autowired
    SpecialistService specialistService;

    @Autowired
    AssistanceService assistanceService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    PasswordEncoder encoder;

    @Bean
    @Profile("!test")
    public CommandLineRunner initialization(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                CustomerInputDTO cus = CustomerInputDTO.builder()
                        .email("ehsan@gmail.com")
                        .password("12345678a")
                        .firstName("ehsan")
                        .lastName("abbasnejad")
                        .build();

                SpecialistInputDTO specialist = SpecialistInputDTO.builder()
                        .email("sao@gmail.com")
                        .firstName("sao")
                        .lastName("alizadeh")
                        .password("12345678a").build();

                User admin = User.builder()
                        .email("admin@admin.admin")
                        .password(encoder.encode("12345678a"))
                        .firstName("admin")
                        .lastName("admin")
                        .role("ADMIN")
                        .registrationDate(new Date())
                        .status(UserStatus.APPROVED)
                        .build();
                userRepository.save(admin);
                specialistService.save(specialist);
                CustomerOutputDTO save = customerService.save(cus);
                User user = userRepository.findById(save.getId()).orElseThrow(() -> new RuntimeException());
                user.setStatus(UserStatus.APPROVED);
                userRepository.save(user);
            }
        };
    }
}
