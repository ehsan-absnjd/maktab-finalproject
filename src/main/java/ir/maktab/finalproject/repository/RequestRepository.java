package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    public List<Request> findAll();
}
