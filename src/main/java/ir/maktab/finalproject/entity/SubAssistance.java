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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubAssistance that = (SubAssistance) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(basePrice, that.basePrice) && Objects.equals(description, that.description) && Objects.equals(assistance, that.assistance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, basePrice, description, assistance);
    }
}
