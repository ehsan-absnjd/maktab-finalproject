package ir.maktab.finalproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "subassistance_id")
    private SubAssistance subAssistance;

    @NotNull
    private Double offeredPrice;

    @NotNull
    private String description;

    @NotNull
    private Date registerDate;

    @NotNull
    private Date executionDate;

    @NotNull
    private String address;

    @NotNull
    private OrderStatus status;

    @OneToMany
    @JoinColumn(name = "order_id")
    private Set<Offer> offers;

    @OneToOne
    @JoinColumn(name = "offer_id")
    private Offer selectedOffer;

    private Double points;

    private String comment;
}