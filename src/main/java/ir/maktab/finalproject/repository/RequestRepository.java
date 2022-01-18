package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.Offer;
import ir.maktab.finalproject.entity.Request;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r WHERE EXISTS ( SELECT s FROM Specialist s WHERE r.subAssistance.assistance MEMBER OF s.assistances AND s.id=:specialistId) ORDER BY r.registerDate DESC ")
    public List<Request> findForSpecialist(Long specialistId , Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.customer.id=:customerId ORDER BY r.registerDate DESC ")
    public List<Request> findByCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.selectedOffer.specialist.id=:specialistId ORDER BY r.registerDate DESC ")
    public List<Request> findBySpecialistId(Long specialistId, Pageable pageable);
}
