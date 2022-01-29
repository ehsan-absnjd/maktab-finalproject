package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.RestTestConfig;
import ir.maktab.finalproject.RestControllerTest;
import ir.maktab.finalproject.controller.dto.AssistanceInputParam;
import ir.maktab.finalproject.controller.dto.SubAssistanceInputParam;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@ActiveProfiles("test")
@WebMvcTest(controllers = AssistaneceController.class)
@SpringJUnitConfig(RestTestConfig.class)
class AssistaneceControllerTest extends RestControllerTest {
    @Test
    @WithUserDetails("3")
    public void whenAddingAssistance_shouldGetTheRightResponse() throws Exception {
        AssistanceInputParam asistance = new AssistanceInputParam();
        asistance.setTitle("title1");
        mvc.perform(post("/assistances").contentType(MediaType.APPLICATION_JSON).content(toJson(asistance)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("assistance created successfully."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    public void whenGettingAssistance_shouldGetTheRightResponse() throws Exception {
        mvc.perform(get("/assistances").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("3")
    public void whenAddingSubAssistance_shouldGetTheRightResponse() throws Exception {
        SubAssistanceInputParam subAssistanceInputParam = new SubAssistanceInputParam();
        subAssistanceInputParam.setTitle("title");
        subAssistanceInputParam.setBasePrice(14d);
        subAssistanceInputParam.setDescription("description");
        mvc.perform(post("/assistances/1/subassistances").contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(subAssistanceInputParam)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("subassistance created successfully."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    public void whenGettingSubAssistances_shouldGetTheRightResponse() throws Exception {
        mvc.perform(get("/assistances/1/subassistances").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

}