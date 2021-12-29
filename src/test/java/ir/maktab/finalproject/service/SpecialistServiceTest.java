package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.Specialist;
import ir.maktab.finalproject.entity.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class SpecialistServiceTest {

    @Autowired
    SpecialistService service;

    @Autowired
    AssistanceService assistanceService;

    @Test
    public void whenSavingNewSpecialist_shouldBeAbleToRetrieveIt(){
        Specialist saved = saveSpecialistAndReturn();
        Optional<Specialist> retrieved = service.findById(saved.getId());
        assertNotNull(retrieved.get());
        assertEquals(retrieved.get() , saved);
    }

    @Test
    public void whenChangingPassword_shouldGetItRight(){
        Specialist saved = saveSpecialistAndReturn();
        service.changePassword(saved , "newpassword");
        Optional<Specialist> retrieved = service.findById(saved.getId());
        assertEquals(retrieved.get().getPassword() , "newpassword");
    }

    @Test
    public void whenAddingAssistance_shouldBeAbleToRetrieve(){
        Specialist saved = saveSpecialistAndReturn();
        Assistance assistance = Assistance.builder().title("assistance1").build();
        assistanceService.save(assistance);
        service.addAssistance(saved,assistance);
        Optional<Specialist> retrieved = service.findById(saved.getId());
        assertTrue(retrieved.get().getAssistances().contains(assistance));

    }

    @Test
    public void whenSavingNewSpecialist_shouldBeAbleToRetrieveItByFirstName(){
        Specialist saved = saveSpecialistAndReturn();
        Page<Specialist> retrieved = service.findByFirstName(saved.getFirstName() , Pageable.ofSize(10) );
        assertEquals(retrieved.get().findAny().get() , saved);
    }

    @Test
    public void whenSavingNewSpecialist_shouldBeAbleToRetrieveItByLastName(){
        Specialist saved = saveSpecialistAndReturn();
        Page<Specialist> retrieved = service.findByLastName(saved.getLastName() , Pageable.ofSize(10) );
        assertEquals(retrieved.get().findAny().get() , saved);
    }

    @Test
    public void whenSavingNewSpecialist_shouldBeAbleToRetrieveItByEmail(){
        Specialist saved = saveSpecialistAndReturn();
        Page<Specialist> retrieved = service.findByEmail(saved.getEmail() , Pageable.ofSize(10) );
        assertEquals(retrieved.get().findAny().get() , saved);
    }

    @Test
    public void whenSavingNewSpecialist_shouldBeAbleToRetrieveItByAssistance(){
        Specialist saved = saveSpecialistAndReturn();
        Assistance assistance = Assistance.builder().title("assistance1").build();
        Assistance savedAssistance = assistanceService.save(assistance);
        service.addAssistance(saved,assistance);
        Page<Specialist> retrieved = service.findByAssistanceId(assistance, Pageable.ofSize(10));
        assertEquals(retrieved.get().findAny().get() , saved);
    }

    public Specialist saveSpecialistAndReturn(){
        Specialist specialist = Specialist.builder().firstName("ali").lastName("reza")
                .email("ali@rezaei.ir").password("12345678a")
                .registrationDate(new Date()).status(UserStatus.NEW).credit(111d).build();
        return service.save(specialist);
    }

}