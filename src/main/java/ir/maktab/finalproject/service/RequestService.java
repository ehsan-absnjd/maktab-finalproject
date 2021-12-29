package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.*;
import ir.maktab.finalproject.exception.RequestSettlementException;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.repository.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService extends BaseService<Request,Long>{

    @Autowired
    SpecialistRepository specialistRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    @Qualifier("requestRepository")
    protected void setRepository(JpaRepository<Request, Long> repository) {
        this.repository = repository;
    }

    @Transactional
    public void settle(Request request){
        Offer selectedOffer = request.getSelectedOffer();
        Specialist specialist = selectedOffer.getSpecialist();
        Customer customer = request.getCustomer();
        Double price = selectedOffer.getPrice();
        if (request.getStatus()== RequestStatus.PAID  )
            throw new RequestSettlementException("request is already paid");
        if (customer.getCredit() < price)
            throw new RequestSettlementException("customer credit is not enough");
        specialist.setCredit(specialist.getCredit() + price);
        customer.setCredit(customer.getCredit() - price);
        request.setStatus(RequestStatus.PAID);
        specialistRepository.save(specialist);
        customerRepository.save(customer);
        repository.save(request);
    }
}
