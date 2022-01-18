package ir.maktab.finalproject.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Builder
@Setter
@Getter
public class AssistanceInputParam {
    @NotNull
    private String title;
}
