package ir.maktab.finalproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@DataJpaTest
class AssistanceServiceTest {

    @TestConfiguration
    @ComponentScan("ir.maktab")

    public static class configloader{}

    @Autowired
    AssistanceService service;

    @Test
    public void doNothing(){
        service.findByTitle("ali");

        service.findAll();
    }


}