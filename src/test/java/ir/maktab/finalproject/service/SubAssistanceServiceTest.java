package ir.maktab.finalproject.service;

import ir.maktab.finalproject.TestConfig;
import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.SubAssistance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(TestConfig.class)
class SubAssistanceServiceTest {

    @Autowired
    SubAssistanceService service;

    @Autowired
    AssistanceService assistanceService;

    Assistance assistance;

    @BeforeEach
    public void setup(){
        Assistance assistance = Assistance.builder().title("assistance1").build();
        this.assistance = assistanceService.save(assistance);
    }

    @Test
    public void whenSavingNewSubAssistance_shouldBeAbleToRetrieveIt(){
        SubAssistance subAssistance = saveNewAssistanceAndReturn();
        Optional<SubAssistance> optional = service.findById(subAssistance.getId());
        assertNotNull(optional.get() );
        assertEquals(optional.get() ,subAssistance);
        assertEquals(optional.get().getAssistance() , assistance);
    }

    private SubAssistance saveNewAssistanceAndReturn() {
        SubAssistance subAssistance= SubAssistance.builder().title("subassistance1").assistance(assistance).build();
        return service.save(subAssistance);
    }


}