package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.service.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.service.dto.input.SpecialistInputDTO;
import ir.maktab.finalproject.service.dto.output.AssistanceOutputDTO;
import ir.maktab.finalproject.service.dto.output.SpecialistOutputDTO;
import ir.maktab.finalproject.entity.Specialist;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.SpecialistNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class SpecialistServiceTest {
    @Autowired
    SpecialistService service;

    @Autowired
    AssistanceService assistanceService;

    @Autowired
    UserService userService;

    @Autowired
    TestHelper helper;

    @Autowired
    PasswordEncoder encoder;

    @Test
    public void whenSavingNewSpecialist_shouldBeAbleToRetrieveIt(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO saved = service.save(specialistInputDTO1);
        SpecialistOutputDTO retrieved = service.findById(saved.getId());
        helper.testEquality(specialistInputDTO1 , retrieved);
    }

    @Test
    public void whenSavingNewSpecialist_RetrievedPasswordShouldBeOk() {
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO saved = service.save(specialistInputDTO1);
        Specialist specialist = service.getById(saved.getId());
        assertTrue(encoder.matches(specialistInputDTO1.getPassword(), specialist.getPassword()));
    }

    @Test
    public void whenChangingPassword_shouldGetItRight(){
        String newPassword = "newpass";
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO saved = service.save(specialistInputDTO1);
        service.changePassword(saved.getId(), newPassword);
        Specialist specialist = service.getById(saved.getId());
        assertTrue(encoder.matches(newPassword, specialist.getPassword()));
    }

    @Test
    public void whenSavingTwoSpecialists_shouldBeAbleToRetrievedThem(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistInputDTO specialistInputDTO2 = helper.getSpecialistInputDTO2();
        service.save(specialistInputDTO1);
        service.save(specialistInputDTO2);
        List<SpecialistOutputDTO> all1 = service.findAll();
        List<SpecialistOutputDTO> all2 = service.findAll(PageRequest.of(0, 10));
        assertEquals(2, all1.size() );
        assertEquals(2 , all2.size());
    }

    @Test
    public void whenChangingStatus_statusShouldBeUpdated(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO saved = service.save(specialistInputDTO1);
        service.changeStatus(saved.getId(), UserStatus.APPROVED);
        SpecialistOutputDTO retrieved = service.findById(saved.getId());
        assertEquals(retrieved.getStatus() , UserStatus.APPROVED);
    }

    @Test
    public void whenUpdatingSpecialist_itsDataShouldBeUpdated(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistInputDTO specialistInputDTO2 = helper.getSpecialistInputDTO2();
        SpecialistOutputDTO saved = service.save(specialistInputDTO1);
        service.update(saved.getId(), specialistInputDTO2);
        SpecialistOutputDTO retrieved = service.findById(saved.getId());
        helper.testEquality(specialistInputDTO2,retrieved);
    }

    @Test
    public void whenGettingTheRemovedSpecialist_shouldThrowException(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO saved = service.save(specialistInputDTO1);
        service.removeById(saved.getId());
        assertThrows(SpecialistNotFoundException.class, ()->service.findById(saved.getId()) );
    }

    @Test
    public void whenRetrievingByFirstName_shouldGetTheCorrectResult(){
        String firstName = "faramarz";
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        specialistInputDTO1.setFirstName(firstName);
        SpecialistInputDTO specialistInputDTO2 = helper.getSpecialistInputDTO2();
        SpecialistOutputDTO target = service.save(specialistInputDTO1);
        service.save(specialistInputDTO2);
        List<SpecialistOutputDTO> retrieved = service.findByFirstName(firstName, PageRequest.of(0, 10));
        assertEquals(1,retrieved.size());
        helper.testEquality(target,retrieved.get(0));
    }

    @Test
    public void whenRetrievingByLastName_shouldGetTheCorrectResult(){
        String lastName = "alinejad";
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        specialistInputDTO1.setLastName(lastName);
        SpecialistInputDTO specialistInputDTO2 = helper.getSpecialistInputDTO2();
        SpecialistOutputDTO target = service.save(specialistInputDTO1);
        service.save(specialistInputDTO2);
        List<SpecialistOutputDTO> retrieved = service.findByLastName(lastName, PageRequest.of(0, 10));
        assertEquals(1,retrieved.size());
        helper.testEquality(target,retrieved.get(0));
    }

    @Test
    public void whenRetrievingByEmail_shouldGetTheCorrectResult(){
        String email = "somemail@mail.org";
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        specialistInputDTO1.setEmail(email);
        SpecialistInputDTO specialistInputDTO2 = helper.getSpecialistInputDTO2();
        SpecialistOutputDTO target = service.save(specialistInputDTO1);
        service.save(specialistInputDTO2);
        List<SpecialistOutputDTO> retrieved = service.findByEmail(email, PageRequest.of(0, 10));
        assertEquals(1,retrieved.size());
        helper.testEquality(target,retrieved.get(0));
    }

    @Test
    public void whenAddingAssistance_shouldBeAbleToRetrieve(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO target = service.save(specialistInputDTO1);
        AssistanceInputDTO assistanceInpputDTO1 = helper.getAssistanceInputDTO1();
        AssistanceOutputDTO savedAssistance = assistanceService.save(assistanceInpputDTO1);
        service.addAssistance(target.getId() , savedAssistance.getId());
        SpecialistInputDTO specialistInputDTO2 = helper.getSpecialistInputDTO2();
        service.save(specialistInputDTO2);
        List<SpecialistOutputDTO> retrieved = service.findByAssistanceId(savedAssistance.getId(), PageRequest.of(0, 10));
        List<AssistanceOutputDTO> bySpecialistId = assistanceService.findBySpecialistId(target.getId());
        assertEquals(1,retrieved.size());
        helper.testEquality(target,retrieved.get(0));
        assertEquals(1, bySpecialistId.size());
        assertEquals(assistanceInpputDTO1.getTitle() , bySpecialistId.get(0).getTitle());
    }

    @Test
    public void whenRemovingAssistance_shouldNotBeAbleToRetrieve(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO target = service.save(specialistInputDTO1);
        AssistanceInputDTO assistanceInpputDTO1 = helper.getAssistanceInputDTO1();
        AssistanceOutputDTO savedAssistance = assistanceService.save(assistanceInpputDTO1);
        service.addAssistance(target.getId() , savedAssistance.getId());
        service.removeAssistance(target.getId(), savedAssistance.getId());
        List<SpecialistOutputDTO> retrieved = service.findByAssistanceId(savedAssistance.getId(), PageRequest.of(0, 10));
        assertEquals(0,retrieved.size());
    }

    @Test
    public void whenApprovingTheWaitingSpecialist_itsStatusShouldBeAppproved(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO target = service.save(specialistInputDTO1);
        userService.verify(target.getId(), target.getEmail());
        SpecialistOutputDTO toBeApproved = service.approve(target.getId());
        assertEquals(UserStatus.APPROVED , toBeApproved.getStatus());
    }


    @Test
    public void whenChangingSpecialistPhotoUrl_itShouldBeUpdated(){
        SpecialistInputDTO specialistInputDTO1 = helper.getSpecialistInputDTO1();
        SpecialistOutputDTO target = service.save(specialistInputDTO1);
        String newUrl = "newurl";
        service.changePhotoUrl(target.getId(), newUrl);
        SpecialistOutputDTO specialist = service.findById(target.getId());
        assertEquals(newUrl ,specialist.getPhotoURL());
    }
}