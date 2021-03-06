package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.SubAssistance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubAssistanceRepository extends JpaRepository<SubAssistance , Long> {

    @Query("SELECT s FROM SubAssistance s WHERE s.id=:subAssistanceId AND s.assistance.id = :assistanceId")
    public Optional<SubAssistance> findByAssistanceIdAndSubAssistanceId(Long assistanceId, Long subAssistanceId);

    @Query("SELECT s FROM SubAssistance s WHERE s.assistance.id = :assistanceId")
    public List<SubAssistance> findByAssistanceId(Long assistanceId);

    @Query("SELECT s FROM SubAssistance s WHERE s.assistance.id = :assistanceId")
    public Page<SubAssistance> findByAssistanceId(Long assistanceId , Pageable pageable);
}
