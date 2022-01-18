package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.service.dto.input.*;
import ir.maktab.finalproject.service.dto.output.*;
import lombok.experimental.Helper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class UserServiceTest {
    @Autowired
    UserService service;

    @Autowired
    CustomerService customerService;

    @Autowired
    SpecialistService specialistService;

    @Autowired
    TestHelper helper;

    @BeforeEach
    public void setup(){
        CustomerInputDTO customerInputDTO1 = helper.getCustomerInputDTO1();
        CustomerOutputDTO savedCustomer = customerService.save(customerInputDTO1);
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO savedSpecialist = specialistService.save(specialistInputDTO1);
    }
    @Test
    public void whenRetrievingUsers_itShouldContainAllTheAddedUsers(){
        List<UserOutputDTO> all = service.findByParameters(new HashMap<>());
        assertEquals(2,all.size());
        assertTrue(all.stream().filter(a-> a instanceof CustomerOutputDTO ).findAny().isPresent() );
        assertTrue(all.stream().filter(a-> a instanceof SpecialistOutputDTO ).findAny().isPresent() );
    }
}