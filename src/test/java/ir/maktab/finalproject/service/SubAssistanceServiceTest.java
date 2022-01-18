package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.TestHelper;
import ir.maktab.finalproject.service.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.service.dto.input.SubAssistanceInputDTO;
import ir.maktab.finalproject.service.dto.output.AssistanceOutputDTO;
import ir.maktab.finalproject.service.dto.output.SubAssistanceOutputDTO;
import ir.maktab.finalproject.exception.SubAssistanceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
class SubAssistanceServiceTest {
    @Autowired
    SubAssistanceService service;

    @Autowired
    AssistanceService assistanceService;

    @Autowired
    TestHelper helper;

    Long assistanceId;

    @BeforeEach
    public void setup(){
        AssistanceInputDTO assistanceInpputDTO1 = helper.getAssistanceInputDTO1();
        AssistanceOutputDTO saved = assistanceService.save(assistanceInpputDTO1);
        assistanceId = saved.getId();
    }

    @Test
    public void whenSavingNewSubAssistance_shouldBeAbleToRetrieveIt(){
        SubAssistanceInputDTO subAssistanceInputDTO1 = helper.getSubAssistanceInputDTO1();
        SubAssistanceOutputDTO saved = service.save(assistanceId, subAssistanceInputDTO1);
        SubAssistanceOutputDTO retrieved = service.findById(assistanceId, saved.getId());
        helper.testEquality(subAssistanceInputDTO1 , retrieved);
    }

    @Test
    public void whenSavingTwoSubAssistances_shouldBeAbleToRetrieveThem(){
        SubAssistanceInputDTO subAssistanceInputDTO1 = helper.getSubAssistanceInputDTO1();
        SubAssistanceInputDTO subAssistanceInputDTO2 = helper.getSubAssistanceInputDTO2();
        service.save(assistanceId,subAssistanceInputDTO1);
        service.save(assistanceId,subAssistanceInputDTO2);
        List<SubAssistanceOutputDTO> all1 = service.findAll(assistanceId);
        List<SubAssistanceOutputDTO> all2 = service.findAll(assistanceId, PageRequest.of(0, 10));
        assertEquals(2, all1.size());
        assertEquals(2, all2.size());
    }

    @Test
    public void whenUpdatingSubAssistance_dataShouldBeUpdated(){
        SubAssistanceInputDTO subAssistanceInputDTO1 = helper.getSubAssistanceInputDTO1();
        SubAssistanceInputDTO subAssistanceInputDTO2 = helper.getSubAssistanceInputDTO2();
        SubAssistanceOutputDTO saved = service.save(assistanceId, subAssistanceInputDTO1);
        service.update(assistanceId , saved.getId() , subAssistanceInputDTO2);
        SubAssistanceOutputDTO retrieved = service.findById(assistanceId, saved.getId());
        helper.testEquality(subAssistanceInputDTO2 , retrieved);
    }

    @Test
    public void whenRemovingSubAssistance_shouldNotBeAbleToRetrieveIt(){
        SubAssistanceInputDTO subAssistanceInputDTO1 = helper.getSubAssistanceInputDTO1();
        SubAssistanceOutputDTO saved = service.save(assistanceId, subAssistanceInputDTO1);
        service.removeById(assistanceId , saved.getId());
        assertThrows(SubAssistanceNotFoundException.class,()-> service.findById(assistanceId, saved.getId()));
    }
}