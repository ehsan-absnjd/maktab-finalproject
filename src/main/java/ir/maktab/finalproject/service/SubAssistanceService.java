package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.SubAssistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SubAssistanceService extends BaseService<SubAssistance,Long>{

    @Autowired
    @Qualifier("subAssistanceRepository")
    protected void setRepository(JpaRepository<SubAssistance, Long> repository) {
        this.repository = repository;
    }
}
