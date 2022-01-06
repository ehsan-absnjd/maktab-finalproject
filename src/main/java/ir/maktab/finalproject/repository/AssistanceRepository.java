package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Assistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssistanceRepository extends JpaRepository<Assistance, Long> {
    public List<Assistance> findAll();
}
