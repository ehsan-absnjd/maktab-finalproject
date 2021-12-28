package ir.maktab.finalproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Specialist specialist;

    @NotNull
    private Date registerDate;

    @NotNull
    private Double price;

    @NotNull
    private Double executionPeriod;

    @NotNull
    private Date beginning;

    @ManyToOne(fetch = FetchType.LAZY)
    private Request request;
}
