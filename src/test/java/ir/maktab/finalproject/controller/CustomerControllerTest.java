package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.AuthTestConfig;
import ir.maktab.finalproject.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@WebMvcTest(controllers = CustomerController.class)
@SpringJUnitConfig(AuthTestConfig.class)
class CustomerControllerTest extends RestControllerTest{

    @Test
    @WithUserDetails("1")
    public void ok(){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
    }


}