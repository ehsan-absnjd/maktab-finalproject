package ir.maktab.finalproject;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan({"ir.maktab.finalproject.entity","ir.maktab.finalproject.repository","ir.maktab.finalproject.service","ir.maktab.finalproject.util"}  )
public class TestConfig {
    @Bean
    public TestHelper testHelper(){
        return new TestHelper();
    }
}
