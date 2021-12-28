package ir.maktab.finalproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specialist extends User {
    @NotNull
    private String photoURL;

    @NotNull
    private Double points;

    @ManyToMany
    @JoinTable(name = "specialist_assistance")
    private Set<Assistance> assistances;

    public void addAssistance(Assistance assistance){
        assistances.add(assistance);
    }

    public void removeAssistance(Assistance assistance){
        assistances.remove(assistance);
    }
}
