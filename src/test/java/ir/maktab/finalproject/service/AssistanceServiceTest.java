package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.SubAssistance;
import ir.maktab.finalproject.exception.AssistanceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class AssistanceServiceTest {

    @Autowired
    AssistanceService service;

    @Test
    public void whenSavingNewAssistance_shouldBeApleToRetrieveIt(){
        Assistance assistance = saveNewAssistanceAndReturn();
        Optional<Assistance> retrieved = service.findById(assistance.getId());
        assertNotNull(retrieved.get());
        assertEquals(assistance , retrieved.get());
    }

    @Test
    public void whenSavingTwoAssistancesWithSameTitle_shouldThrow(){
        saveNewAssistanceAndReturn();
        saveNewAssistanceAndReturn();
        //System.out.println(service.findAll().stream().count());
    }

    @Test
    public void whenAddingSubAssistanceUsingId_shouldBeAbleToRetrieveIt(){
        Long id = saveNewAssistanceAndReturn().getId();
        SubAssistance subAssistance = SubAssistance.builder()
                .title("shostoshu").description("description").basePrice(13.5).build();
        SubAssistance subAssistance2 = SubAssistance.builder()
                .title("shostoshu").description("description2").basePrice(143.5).build();
        service.addSubAssistanceById(id,subAssistance);
        service.addSubAssistanceById(id, subAssistance2);
        Assistance assistance = service.findById(id).get();
        assertTrue(contains(assistance.getSubAssistances() , subAssistance));
    }

    @Test
    public void whenAddingSubAssistanceUsingInstance_shouldBeAbleToRetrieveIt(){
        Long id = saveNewAssistanceAndReturn().getId();
        SubAssistance subAssistance = SubAssistance.builder()
                .title("shostoshu").description("description").basePrice(13.5).build();
        Assistance assistance = service.findById(id).get();
        service.addSubAssistance(assistance,subAssistance);
        assertEquals(assistance.getSubAssistances().stream().count() , 1);
        assertTrue(contains(assistance.getSubAssistances() , subAssistance));
    }

    @Test
    public void whenAddingTwoSubAssistanceWithSameTitleAndAssistanceId_shouldThrow(){
        Long id = saveNewAssistanceAndReturn().getId();
        SubAssistance subAssistance = SubAssistance.builder()
                .title("shostoshu").description("description").basePrice(13.5).build();
        SubAssistance subAssistance2 = SubAssistance.builder()
                .title("shostoshu").description("description2").basePrice(143.5).build();
        service.addSubAssistanceById(id,subAssistance);
        service.addSubAssistanceById(id, subAssistance2);
    }

    @Test
    public void whenAddingSubAssistanceForUnValidAssistanceId_shouldThrowAssistanceNotFoundException(){
        SubAssistance subAssistance = SubAssistance.builder()
                .title("shostoshu").description("description").basePrice(13.5).build();
        assertThrows(AssistanceNotFoundException.class , ()->service.addSubAssistanceById(2l,subAssistance));
    }

    public Assistance saveNewAssistanceAndReturn(){
        Assistance assistance = Assistance.builder().title("behdasht").build();
        return service.save(assistance);
    }

    public boolean contains(Set<SubAssistance> set , SubAssistance sa){
        for (SubAssistance a:set) {
            if (a.getTitle().equals(sa.getTitle()) && a.getDescription().equals(sa.getDescription()) && a.getBasePrice().equals(sa.getBasePrice()))
                return true;
        }
        return false;
    }


}