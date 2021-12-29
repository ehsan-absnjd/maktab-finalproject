package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.SubAssistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

public class CustomerService extends BaseService<Customer,Long>{

    @Autowired
    @Qualifier("customerRepository")
    protected void setRepository(JpaRepository<Customer, Long> repository) {
        this.repository = repository;
    }
}
