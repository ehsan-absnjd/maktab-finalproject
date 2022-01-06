package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.Specialist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    public List<Specialist> findAll();

    public List<Specialist> findByFirstName(String firstName, Pageable pageable);

    public List<Specialist> findByLastName(String lastName, Pageable pageable);

    public List<Specialist> findByEmail(String email, Pageable pageable);

    @Query(value = "SELECT s FROM Specialist s where :assistance MEMBER OF s.assistances ORDER BY s.id DESC")
    public List<Specialist> findByAssistanceId(Assistance assistance , Pageable pageable);

    @Modifying(clearAutomatically = true , flushAutomatically = true)
    @Query("UPDATE Specialist s SET s.points = (SELECT AVG (r.points ) FROM Request r WHERE r.selectedOffer.specialist.id=s.id ) WHERE s.id=:specialistId")
    public void updateSpecialistPoints(Long specialistId);
}
