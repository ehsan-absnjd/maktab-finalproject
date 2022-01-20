package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.AuthTestConfig;
import ir.maktab.finalproject.RestControllerTest;
import ir.maktab.finalproject.controller.dto.CustomerRegisterParam;
import ir.maktab.finalproject.service.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.service.dto.output.CustomerOutputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(controllers = CustomerController.class)
@SpringJUnitConfig(AuthTestConfig.class)
class CustomerControllerTest extends RestControllerTest {
    @Test
    public void whenAddingCustomer_shouldGetTheRightResult() throws Exception {
        CustomerRegisterParam customer = CustomerRegisterParam.builder()
                .firstName("ehsan")
                .lastName("abbasnejad")
                .email("ehsan@abbas.nejad")
                .password("ehsanabbas1")
                .credit(135d)
                .build();

        CustomerInputDTO customerInputDTO = CustomerInputDTO.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .password(customer.getPassword())
                .credit(customer.getCredit()).build();

        CustomerOutputDTO customerOutputDTO = CustomerOutputDTO.builder()
                .id(1l)
                .role("CUSTOMER")
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .build();

        //Mockito.when(customerService.save(customerInputDTO)).thenReturn(customerOutputDTO);

        mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(toJson(customer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("customer registered successfully."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.errors").isEmpty());
                //.andExpect(jsonPath("$.data").value(customerOutputDTO));
    }

    @Test
    @WithUserDetails("1")
    public void whenGettingACustomerWithCorrectAccount_shouldGetTheRightResult() throws Exception {
        mvc.perform(get("/customers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
//                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @WithUserDetails("2")
    public void whenGettingACustomerWithWrongAccount_shouldGetForbidden() throws Exception {
        mvc.perform(get("/customers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("1")
    public void whenGettingRequestsWithCorrectAccount_shouldGetTheRightResult() throws Exception {
        mvc.perform(get("/customers/1/requests?status=WAITING_ARRIVAL").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.errors").isEmpty());
//                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @WithUserDetails("2")
    public void whenGettingRequestsWithWrongAccount_shouldGetForbidden() throws Exception {
        mvc.perform(get("/customers/1/requests?status=WAITING_ARRIVAL").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}