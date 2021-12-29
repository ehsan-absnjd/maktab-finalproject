package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.entity.*;
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
class OfferServiceTest {

    @Autowired
    OfferService service;

    @Autowired
    RequestService requestService;

    @Autowired
    CustomerService customerService;

    @Autowired
    SpecialistService specialistService;

    @Test
    public void whenSavingNewOffer_shouldBeAbleToRetrieveIt(){
        Request request = saveRequestAndReturn();
        Specialist specialist = saveSpecialistAndReturn();
        Offer offer = Offer.builder().specialist(specialist).registerDate(new Date()).request(request).beginning(new Date())
                .executionPeriod(1d).price(12d).build();
        Offer saved = service.save(offer);
        Optional<Offer> optional = service.findById(saved.getId());
        assertNotNull(optional.get());
        assertEquals(optional.get() , saved);
    }


    public Customer saveCustomerAndReturn(){
        Customer customer = Customer.builder().firstName("ali").lastName("reza")
                .email("ali@rezaei.ir").password("12345678a")
                .registrationDate(new Date()).status(UserStatus.NEW).credit(111d).build();
        return customerService.save(customer);
    }

    public Specialist saveSpecialistAndReturn(){
        Specialist specialist = Specialist.builder().firstName("hoseyn").lastName("moharami")
                .email("hoseyn@moharami.ir").password("12345678a")
                .registrationDate(new Date()).status(UserStatus.NEW).credit(111d).build();
        return specialistService.save(specialist);
    }

    public Request saveRequestAndReturn(){
        Customer customer = saveCustomerAndReturn();
        Request request = Request.builder().description("description").address("tehran").status(RequestStatus.WAITING_FOR_OFFERS)
                .customer(customer).executionDate(new Date()).offeredPrice(13.5).registerDate(new Date()).build();
        return requestService.save(request);
    }

}