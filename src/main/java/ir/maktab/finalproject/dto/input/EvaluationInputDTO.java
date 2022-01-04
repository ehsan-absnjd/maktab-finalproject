package ir.maktab.finalproject.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class EvaluationInputDTO {

    Double points;

    String comment;

}
