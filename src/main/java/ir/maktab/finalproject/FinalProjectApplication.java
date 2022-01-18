package ir.maktab.finalproject;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.service.AssistanceService;
import ir.maktab.finalproject.service.SpecialistService;
import ir.maktab.finalproject.service.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.service.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.service.CustomerService;
import ir.maktab.finalproject.service.dto.input.SpecialistInputDTO;
import ir.maktab.finalproject.service.dto.output.AssistanceOutputDTO;
import ir.maktab.finalproject.service.dto.output.SpecialistOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class FinalProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }

    @Autowired
    SpecialistService specialistService;

    @Autowired
    AssistanceService assistanceService;

    @Bean
    @Profile("!test")
    public CommandLineRunner initialization(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                SpecialistInputDTO cus = SpecialistInputDTO.builder()
                        .email("ehsan@gmail.com")
                        .password("12345678a")
                        .firstName("ehsan")
                        .lastName("abbasnejad")
                        .credit(13.5)
                        .build();
                SpecialistOutputDTO saved = specialistService.save(cus);
                AssistanceOutputDTO assistance = assistanceService.save(AssistanceInputDTO.builder().title("a1").build());
                specialistService.addAssistance(saved.getId(), assistance.getId());
            }
        };
    }
}
