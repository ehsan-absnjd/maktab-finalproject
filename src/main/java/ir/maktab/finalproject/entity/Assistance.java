package ir.maktab.finalproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
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
}
