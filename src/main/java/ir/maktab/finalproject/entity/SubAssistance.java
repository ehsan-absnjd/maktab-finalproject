package ir.maktab.finalproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubAssistance{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String title;

    @NotNull
    private Double basePrice;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Assistance assistance;
}
