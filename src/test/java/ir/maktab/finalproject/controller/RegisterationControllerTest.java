package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.FinalProjectApplication;
import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.service.*;
import ir.maktab.finalproject.service.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.service.dto.output.UserOutputDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get ;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = RegisterationController.class)
class RegisterationControllerTest extends RestControllerTest{



    @Test
    public void shoutBeOk() throws Exception {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("firstName" , new String[]{"ehsan"});
        Mockito.when(customerService.findAll() ).thenReturn(Arrays.asList(CustomerOutputDTO.builder().build(),CustomerOutputDTO.builder().build()) );

        mvc.perform(get("/test/{0}" , 2  ).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()" , Matchers.is(1)));
    }

}