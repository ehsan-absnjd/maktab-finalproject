package ir.maktab.finalproject.service.dto.output;

import ir.maktab.finalproject.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Setter
@Getter
public class SpecialistOutputDTO extends UserOutputDTO {
    private String photoURL;

    private Double points;
}
