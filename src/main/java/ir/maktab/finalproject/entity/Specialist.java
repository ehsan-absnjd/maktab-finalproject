package ir.maktab.finalproject.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Specialist extends User {
    @NotNull
    private String photoURL;

    @NotNull
    private Double points;

    @ManyToMany
    @JoinTable(name = "specialist_assistance")
    private Set<Assistance> assistances = new HashSet<>();

    public void addAssistance(Assistance assistance){
        assistances.add(assistance);
    }

    public void removeAssistance(Assistance assistance){
        assistances.remove(assistance);
    }
}
