package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.User;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.CustomerNotFoundException;
import ir.maktab.finalproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class UserRepositoryTest {
    @Autowired
    UserRepository repository;

    @Autowired
    CustomerService customerService;

    @Autowired
    SpecialistService specialistService;

    @Autowired
    TestHelper helper;

    @Test
    public void ok(){
        CustomerInputDTO customerInputDTO1 = helper.getCustomerInputDTO1();
        customerService.save(helper.getCustomerInputDTO1());
        specialistService.save(helper.getSpecialistInputDTO1());
        User byEmail = repository.findByEmail(customerInputDTO1.getEmail());
        System.out.println(byEmail.getEmail() + byEmail.getPassword());
        assertNotNull(byEmail);


    }




}