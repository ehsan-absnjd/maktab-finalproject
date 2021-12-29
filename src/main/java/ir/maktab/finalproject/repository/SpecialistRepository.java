package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.Specialist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    Page<Specialist> findByFirstName(String firstName, Pageable pageable);

    Page<Specialist> findByLastName(String lastName, Pageable pageable);

    Page<Specialist> findByEmail(String email, Pageable pageable);

    @Query(value = "SELECT s FROM Specialist s where :assistance MEMBER OF s.assistances ORDER BY s.id DESC")
    Page<Specialist> findByAssistanceId(Assistance assistance , Pageable pageable);

}
