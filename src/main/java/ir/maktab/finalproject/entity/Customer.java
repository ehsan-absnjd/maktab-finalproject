package ir.maktab.finalproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User{

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    @Builder.Default
    private Set<Request> requests = new HashSet<>();

    public void addOrder(Request request){
        requests.add(request);
        request.setCustomer(this);
    }

    public void removeOrder(Request request){
        requests.remove(request);
        request.setCustomer(null);
    }
}
