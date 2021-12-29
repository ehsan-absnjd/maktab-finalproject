package ir.maktab.finalproject.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assistance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String title;

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "assistance_id")
    @Builder.Default
    private Set<SubAssistance> subAssistances=new HashSet<>();

    public void addSubAssistance(SubAssistance subAssistance){
        subAssistances.add(subAssistance);
        subAssistance.setAssistance(this);
    }

    public void removeSubAssistance(SubAssistance subAssistance){
        subAssistances.remove(subAssistance);
        subAssistance.setAssistance(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistance that = (Assistance) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
