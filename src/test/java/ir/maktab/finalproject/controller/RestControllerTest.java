package ir.maktab.finalproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ir.maktab.finalproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;

public abstract class RestControllerTest {
    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected UserService userService;

    @MockBean
    protected SpecialistService specialistService;

    @MockBean
    protected CustomerService customerService;

    @MockBean
    protected AssistanceService assistanceService;

    @MockBean
    protected SubAssistanceService subAssistanceService;

    @MockBean
    protected RequestService requestService;

    @MockBean
    protected OfferService offerService;

    ObjectMapper objectMapper = new ObjectMapper();

    protected String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json , clazz);
    }
}
