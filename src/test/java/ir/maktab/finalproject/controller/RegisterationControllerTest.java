package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.service.dto.output.CustomerOutputDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get ;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
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