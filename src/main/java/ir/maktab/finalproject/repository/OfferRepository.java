package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Offer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> , JpaSpecificationExecutor<Offer> {
    @Query("SELECT o FROM Offer o WHERE o.id =:offerId AND o.request.id=:requestId")
    public Optional<Offer> findByRequestIdAndOfferId(Long requestId, Long offerId);

    @Query("SELECT o FROM Offer o WHERE o.request.id=:requestId")
    public List<Offer> findByRequestId(Long requestId);

    @Query("SELECT o FROM Offer o WHERE o.request.id=:requestId")
    public List<Offer> findByRequestId(Long requestId , Pageable pageable);
}
