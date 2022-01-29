package ir.maktab.finalproject;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@ComponentScan({"ir.maktab.finalproject.entity","ir.maktab.finalproject.repository","ir.maktab.finalproject.service"}  )
public class TestConfig {
    @Bean
    public TestHelper testHelper(){
        return new TestHelper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
