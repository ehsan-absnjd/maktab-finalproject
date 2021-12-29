package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

public class OfferService extends BaseService<Offer,Long>{

    @Autowired
    @Qualifier("offerRepository")
    protected void setRepository(JpaRepository<Offer, Long> repository) {
        this.repository = repository;
    }
}
