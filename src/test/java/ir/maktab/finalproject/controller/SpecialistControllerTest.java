package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.RestTestConfig;
import ir.maktab.finalproject.RestControllerTest;
import ir.maktab.finalproject.controller.dto.AddAssistanceInputParam;
import ir.maktab.finalproject.controller.dto.SpecialistRegisterParam;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(controllers = SpecialistController.class)
@SpringJUnitConfig(RestTestConfig.class)
class SpecialistControllerTest extends RestControllerTest {
    @Test
    public void whenAddingSpecialist_shouldGetTheRightResult() throws Exception {
        SpecialistRegisterParam specialist = SpecialistRegisterParam.builder()
                .firstName("ehsan")
                .lastName("abbasnejad")
                .email("ehsan@abbas.nejad")
                .password("ehsanabbas1")
                .captcha("")
                .build();

        mvc.perform(post("/specialists").contentType(MediaType.APPLICATION_JSON).content(toJson(specialist)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("specialist registered successfully."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("2")
    public void whenGettingASpecialistWithCorrectAccount_shouldGetTheRightResult() throws Exception {
        mvc.perform(get("/specialists/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("1")
    public void whenGettingASpecialistWithWrongAccount_shouldGetForbidden() throws Exception {
        mvc.perform(get("/specialists/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("3")
    public void whenAddingAssistanceToSpecialist_shouldGetTheRightResult() throws Exception {
        AddAssistanceInputParam assistance = new AddAssistanceInputParam();
        assistance.setAssistanceId(1l);
        mvc.perform(post("/specialists/2/assistances").contentType(MediaType.APPLICATION_JSON).content(toJson(assistance)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("assistance added successfully."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("2")
    public void whenGettingRelevantRequestsForSpecialist_shouldGetTheRightResult() throws Exception {
        mvc.perform(get("/specialists/2/relevantrequests").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("2")
    public void whenGettingRequestsOfSpecialist_shouldGetTheRightResult() throws Exception {
        mvc.perform(get("/specialists/2/requests?status=WAITING_ARRIVAL").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("3")
    public void whenApprovingSpecialist_shouldGetTheRightResult() throws Exception {
        mvc.perform(post("/specialists/2/approve").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }
}