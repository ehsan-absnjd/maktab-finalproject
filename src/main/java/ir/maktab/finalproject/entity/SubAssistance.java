package ir.maktab.finalproject.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "title", "assistance_id" }) })
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubAssistance{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Double basePrice;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Assistance assistance;
}
