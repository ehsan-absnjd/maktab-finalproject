package ir.maktab.finalproject;

import ir.maktab.finalproject.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.service.CustomerService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FinalProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }

    @Autowired
    CustomerService customerService;

    @Bean
    public CommandLineRunner initialization(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                CustomerInputDTO cus = CustomerInputDTO.builder()
                        .email("ehsan@gmail.com")
                        .password("12345678a")
                        .firstName("ehsan")
                        .lastName("abbasnejad")
                        .credit(13.5)
                        .build();
                customerService.save(cus);
            }
        };
    }
}
