package ir.maktab.finalproject.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class EvaluationInputParam {
    Double points;

    String comment;
}
