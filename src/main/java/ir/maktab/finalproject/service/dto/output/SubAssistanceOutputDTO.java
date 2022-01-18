package ir.maktab.finalproject.service.dto.output;

import ir.maktab.finalproject.entity.Assistance;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SubAssistanceOutputDTO {
    private Long id;

    private String title;

    private Double basePrice;

    private String description;

    private Long assistanceId;
}
