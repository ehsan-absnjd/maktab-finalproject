package ir.maktab.finalproject;

import ir.maktab.finalproject.dto.input.AssistanceInputDTO;
import ir.maktab.finalproject.entity.Assistance;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
//@SpringJUnitConfig(TestConfig.class)
public class ModelMapperTest {

    @Test
    public void convert_shouldBeOk(){

    }
}
