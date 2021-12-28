package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.repository.AssistanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AssistanceService extends BaseService<Assistance,Long> {

    @Autowired
    @Qualifier("assistanceRepository")
    protected void setRepository(JpaRepository<Assistance, Long> repository) {
        this.repository = repository;
    }

    public Assistance findByTitle(String title){
        return ((AssistanceRepository)repository).findByTitle(title);
    }
}
