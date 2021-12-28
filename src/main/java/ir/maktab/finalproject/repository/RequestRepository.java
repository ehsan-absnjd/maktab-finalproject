package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
