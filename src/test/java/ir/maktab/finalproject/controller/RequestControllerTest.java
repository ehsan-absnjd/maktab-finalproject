package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.RestTestConfig;
import ir.maktab.finalproject.RestControllerTest;
import ir.maktab.finalproject.controller.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Date;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(controllers = RequestController.class)
@SpringJUnitConfig(RestTestConfig.class)
class RequestControllerTest extends RestControllerTest {
    @Test
    @WithUserDetails("1")
    public void whenAddingRequest_shouldGetTheRightResponse() throws Exception {
        RequestInputParam inputParam = new RequestInputParam();
        inputParam.setSubAssistanceId(1l);
        inputParam.setDescription("des");
        inputParam.setAddress("add");
        inputParam.setCustomerId(1l);
        inputParam.setOfferedPrice(23d);
        inputParam.setLatitude(10.0);
        inputParam.setLongitude(10.0);
        inputParam.setExecutionDate(new Date(System.currentTimeMillis() + 1000) );

        mvc.perform(post("/requests").contentType(MediaType.APPLICATION_JSON).content(toJson(inputParam)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("request registered successfully."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("3")
    public void whenGettingRequests_shouldGetTheRightResponse() throws Exception {
        mvc.perform(get("/requests").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("1")
    public void whenAddingEvaluation_shouldGetTheRightResponse() throws Exception {
        EvaluationInputParam inputParam = new EvaluationInputParam();
        inputParam.setComment("comment");
        inputParam.setPoints(10d);

        mvc.perform(post("/requests/1/evaluate").contentType(MediaType.APPLICATION_JSON).content(toJson(inputParam)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("evaluation added successfully."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("2")
    public void whenAddingOfferToRequest_shouldGetTheRightResponse() throws Exception {
        OfferInputParam inputParam = new OfferInputParam();
        inputParam.setBeginning(new Date());
        inputParam.setPrice(12d);
        inputParam.setExecutionPeriod(2d);
        inputParam.setSpecialistId(1l);

        mvc.perform(post("/requests/1/offers").contentType(MediaType.APPLICATION_JSON).content(toJson(inputParam)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("request registered successfully."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("1")
    public void whenGettingOffers_shouldGetTheRightResponse() throws Exception {
        mvc.perform(get("/requests/1/offers?orderby=pointdesc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("1")
    public void whenSelectingOffer_shouldGetTheRightResponse() throws Exception {
        SelectOfferParam param = new SelectOfferParam();
        param.setOfferId(1l);

        mvc.perform(post("/requests/1/selectoffer").contentType(MediaType.APPLICATION_JSON).content(toJson(param)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("offer selected successfully."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("1")
    public void whenPayingRequest_shouldGetTheRightResponse() throws Exception {
        mvc.perform(post("/requests/1/pay").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("payment was successful."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("2")
    public void whenStartingRequest_shouldGetTheRightResponse() throws Exception {
        mvc.perform(post("/requests/1/start").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("request began successfully."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithUserDetails("2")
    public void whenEndingRequest_shouldGetTheRightResponse() throws Exception {
        mvc.perform(post("/requests/1/end").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("request ended successfully."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
    }


}