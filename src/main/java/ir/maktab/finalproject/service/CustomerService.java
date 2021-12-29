package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.SubAssistance;
import ir.maktab.finalproject.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService extends BaseService<Customer,Long>{

    @Autowired
    @Qualifier("customerRepository")
    protected void setRepository(JpaRepository<Customer, Long> repository) {
        this.repository = repository;
    }

    @Transactional
    public void changePassword(Customer customer , String password){
        customer.setPassword(password);
        save(customer);
    }

    @Transactional
    public void changePassword(Long customerId , String password){
        Customer customer = findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
        customer.setPassword(password);
        save(customer);
    }
}
