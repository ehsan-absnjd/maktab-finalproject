package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
