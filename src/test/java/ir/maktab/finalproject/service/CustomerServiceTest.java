package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class CustomerServiceTest {

    @Autowired
    CustomerService service;

    @Test
    public void whenSavingNewCustomer_shouldBeAbleToRetrieveIt(){
        Customer saved = saveCusomerAndReturn();
        Optional<Customer> retrieved = service.findById(saved.getId());
        assertNotNull(retrieved.get());
        assertEquals(retrieved.get() , saved);
    }

    @Test
    public void whenChangingPassword_shouldGetItRight(){
        Customer customer = saveCusomerAndReturn();
        service.changePassword(customer , "newpassword");
        Optional<Customer> retrieved = service.findById(customer.getId());
        assertEquals(retrieved.get().getPassword() , "newpassword");
    }

    public Customer saveCusomerAndReturn(){
        Customer customer = Customer.builder().firstName("ali").lastName("reza")
                .email("ali@rezaei.ir").password("12345678a")
                .registrationDate(new Date()).status(UserStatus.NEW).credit(111d).build();
        return service.save(customer);
    }

}