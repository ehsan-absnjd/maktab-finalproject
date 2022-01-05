package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.dto.input.CustomerInputDTO;
import ir.maktab.finalproject.dto.output.CustomerOutputDTO;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.CustomerNotFoundException;
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
class CustomerServiceTest {
    @Autowired
    CustomerService service;

    @Autowired
    TestHelper helper;

    @Test
    public void whenSavingNewCustomer_shouldBeAbleToRetrieveIt(){
        CustomerInputDTO customerInputDTO = helper.getCustomerInputDTO1();
        CustomerOutputDTO saved = service.save(customerInputDTO);
        CustomerOutputDTO retrieved = service.findById(saved.getId());
        helper.testEquality(customerInputDTO, retrieved);
        assertEquals(retrieved.getStatus() , UserStatus.NEW);
    }

    @Test
    public void whenSavingNewCustomer_RetrievedPasswordShouldBeOk(){
        CustomerInputDTO customerInputDTO = helper.getCustomerInputDTO1();
        CustomerOutputDTO saved = service.save(customerInputDTO);
        Customer customer = service.getById(saved.getId());
        assertEquals( customerInputDTO.getPassword() , customer.getPassword());
    }

    @Test
    public void whenChangingPassword_shouldGetItRight(){
        String newPassword = "newpassword";
        CustomerInputDTO inputDTO = helper.getCustomerInputDTO1();
        CustomerOutputDTO saved = service.save(inputDTO);
        service.changePassword(saved.getId(), newPassword);
        Customer customer = service.getById(saved.getId());
        assertEquals(newPassword,customer.getPassword());
    }

    @Test
    public void whenSavingTwoCustomers_shouldBeAbleToRetrievedThem(){
        CustomerInputDTO customerInputDTO1 = helper.getCustomerInputDTO1();
        CustomerInputDTO customerInputDTO2 = helper.getCustomerInputDTO2();
        service.save(customerInputDTO1);
        service.save(customerInputDTO2);
        List<CustomerOutputDTO> all1 = service.findAll();
        List<CustomerOutputDTO> all2 = service.findAll(PageRequest.of(0, 10));
        assertEquals(2, all1.size() );
        assertEquals(2 , all2.size());
    }

    @Test
    public void whenChangingStatus_statusShouldBeUpdated(){
        CustomerInputDTO customerInputDTO1 = helper.getCustomerInputDTO1();
        CustomerOutputDTO saved = service.save(customerInputDTO1);
        service.changeStatus(saved.getId(), UserStatus.APPROVED);
        CustomerOutputDTO retrieved = service.findById(saved.getId());
        assertEquals(UserStatus.APPROVED , retrieved.getStatus() );
    }

    @Test
    public void whenUpdatingCustomer_itsDataShouldBeUpdated(){
        CustomerInputDTO customerInputDTO1 = helper.getCustomerInputDTO1();
        CustomerInputDTO customerInputDTO2 = helper.getCustomerInputDTO2();
        CustomerOutputDTO saved = service.save(customerInputDTO1);
        service.update(saved.getId(), customerInputDTO2);
        CustomerOutputDTO retrieved = service.findById(saved.getId());
        helper.testEquality(customerInputDTO2 , retrieved);
    }

    @Test
    public void whenGettingTheRemovedCustomer_shouldThrowException(){
        CustomerInputDTO customerInputDTO = helper.getCustomerInputDTO1();
        CustomerOutputDTO saved = service.save(customerInputDTO);
        service.removeById(saved.getId());
        assertThrows(CustomerNotFoundException.class , ()-> service.getById(saved.getId()));
    }
}