package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.SubAssistance;
import ir.maktab.finalproject.exception.AssistanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AssistanceService extends BaseService<Assistance,Long> {

    @Autowired
    @Qualifier("assistanceRepository")
    protected void setRepository(JpaRepository<Assistance, Long> repository) {
        this.repository = repository;
    }

    @Transactional
    public void addSubAssistance(Assistance assistance, SubAssistance subAssistance){
        assistance.addSubAssistance(subAssistance);
        repository.save(assistance);
    }

    @Transactional
    public void addSubAssistanceById(Long assistanceId, SubAssistance subAssistance){
        Optional<Assistance> optional = repository.findById(assistanceId);
        Assistance assistance = optional.orElseThrow(() -> new AssistanceNotFoundException());
        assistance.addSubAssistance(subAssistance);
        repository.save(assistance);
    }

}
