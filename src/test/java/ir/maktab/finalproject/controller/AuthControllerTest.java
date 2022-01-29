package ir.maktab.finalproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ir.maktab.finalproject.RestTestConfig;
import ir.maktab.finalproject.RestControllerTest;
import ir.maktab.finalproject.controller.dto.LoginInputParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = AuthController.class)
@SpringJUnitConfig(RestTestConfig.class)
class AuthControllerTest extends RestControllerTest {
    @Value("${secret}")
    String secret;

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
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String token = JWT.create()
                .withSubject("1")
                .withClaim("email", "1")
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .sign(algorithm);
        mvc.perform(post("/auth/refreshtoken").header("Authorization" , "Bearer "+token) )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("token regenerated successfully."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.access_token").isNotEmpty());
    }
}