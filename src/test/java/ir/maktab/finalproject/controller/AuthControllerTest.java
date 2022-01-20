package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.AuthTestConfig;
import ir.maktab.finalproject.RestControllerTest;
import ir.maktab.finalproject.controller.dto.LoginInputParam;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = AuthController.class)
@SpringJUnitConfig(AuthTestConfig.class)
class AuthControllerTest extends RestControllerTest {
    @Test
    public void whenSendingLoginRequest_shouldGetTheJWT() throws Exception {
        LoginInputParam loginInputParam = LoginInputParam.builder().email("1").password("password1").build();
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(toJson(loginInputParam)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("login was successful."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.access_token").isNotEmpty())
                .andExpect(jsonPath("$.data.refresh_token").isNotEmpty());
    }

    @Test
    public void whenSendingRefreshTokenRequest_shouldGetTheJWT() throws Exception {
        LoginInputParam loginInputParam = LoginInputParam.builder().email("1").password("password1").build();
        String contentAsString = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(toJson(loginInputParam)))
                .andReturn().getResponse().getContentAsString();
        int lastIndex=contentAsString.lastIndexOf('"');
        int secondLast = contentAsString.lastIndexOf('"' , lastIndex-1);
        String refreshtoken = contentAsString.substring(secondLast+1, lastIndex);

        TestRestTemplate testRestTemplate = new TestRestTemplate();
        mvc.perform(post("/auth/refreshtoken").header("Authorization" , "Bearer "+refreshtoken) )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("token regenerated successfully."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.access_token").isNotEmpty());
    }
}