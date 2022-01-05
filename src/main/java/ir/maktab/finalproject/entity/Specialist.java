package ir.maktab.finalproject.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Specialist extends User {
    private String photoURL;

    private Double points;

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "specialist_assistance")
    private Set<Assistance> assistances = new HashSet<>();

    public void addAssistance(Assistance assistance){
        assistances.add(assistance);
    }

    public void removeAssistance(Assistance assistance){
        assistances.remove(assistance);
    }
}
