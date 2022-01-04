package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Assistance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssistanceRepository extends JpaRepository<Assistance, Long> {

}
