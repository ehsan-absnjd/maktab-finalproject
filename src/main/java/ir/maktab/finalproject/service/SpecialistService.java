package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.Specialist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

public class SpecialistService extends BaseService<Specialist,Long>{

    @Autowired
    @Qualifier("specialistRepository")
    protected void setRepository(JpaRepository<Specialist, Long> repository) {
        this.repository = repository;
    }
}
