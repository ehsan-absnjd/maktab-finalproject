package ir.maktab.finalproject.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @NotNull
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
    private RequestStatus status;

    private Date beginTime;

    private Date finishTime;

    @OneToMany
    @JoinColumn(name = "order_id")
    @Builder.Default
    private Set<Offer> offers = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "offer_id")
    private Offer selectedOffer;

    private Double points;

    private String comment;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    public void addOffer(Offer offer){
        offers.add(offer);
        offer.setRequest(this);
    }

    public void removeOffer(Offer offer){
        offers.remove(offer);
        offer.setRequest(null);
    }
}