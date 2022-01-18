package ir.maktab.finalproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.maktab.finalproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class RestControllerTest {
    @Autowired
    protected MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    SpecialistService specialistService;

    @MockBean
    AssistanceService assistanceService;

    @MockBean
    CustomerService customerService;

    @MockBean
    AppUserDetailService userDetailService;

    ObjectMapper objectMapper = new ObjectMapper();

    protected String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json , clazz);
    }
}
