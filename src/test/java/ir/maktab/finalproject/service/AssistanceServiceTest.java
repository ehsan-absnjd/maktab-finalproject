package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.dto.output.AssistanceOutputDTO;
import ir.maktab.finalproject.exception.AssistanceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class AssistanceServiceTest {
    @Autowired
    AssistanceService service;

    @Autowired
    TestHelper helper;

    @Test
    public void whenSavingNewAssistance_shouldBeAbleToRetrieveIt(){
        AssistanceInputDTO assistanceInpputDTO1 = helper.getAssistanceInputDTO1();
        AssistanceOutputDTO saved = service.save(assistanceInpputDTO1);
        AssistanceOutputDTO retrieved = service.findById(saved.getId());
        assertEquals(assistanceInpputDTO1.getTitle() , retrieved.getTitle());
    }

    @Test
    public void whenAddingTwoAssistances_shouldBeAbleToRetrieveThem(){
        AssistanceInputDTO assistanceInpputDTO1 = helper.getAssistanceInputDTO1();
        AssistanceInputDTO assistanceInpputDTO2 = helper.getAssistanceInputDTO2();
        service.save(assistanceInpputDTO1);
        service.save(assistanceInpputDTO2);
        List<AssistanceOutputDTO> all1 = service.findAll();
        List<AssistanceOutputDTO> all2 = service.findAll(PageRequest.of(0, 10));
        assertEquals(2,all1.size());
        assertEquals(2,all2.size());
    }

    @Test
    public void whenUpdatingAssistance_itsDataShouldBeUpdated(){
        AssistanceInputDTO assistanceInpputDTO1 = helper.getAssistanceInputDTO1();
        AssistanceInputDTO assistanceInpputDTO2 = helper.getAssistanceInputDTO2();
        AssistanceOutputDTO saved = service.save(assistanceInpputDTO1);
        service.update(saved.getId() , assistanceInpputDTO2);
        AssistanceOutputDTO retrieved = service.findById(saved.getId());
        assertEquals(assistanceInpputDTO2.getTitle() , retrieved.getTitle());
    }

    @Test
    public void whenRemovingAssistance_shouldThrowException(){
        AssistanceInputDTO assistanceInpputDTO1 = helper.getAssistanceInputDTO1();
        AssistanceOutputDTO saved = service.save(assistanceInpputDTO1);
        service.removeById(saved.getId());
        assertThrows(AssistanceNotFoundException.class ,()->service.findById(saved.getId()));
    }
}