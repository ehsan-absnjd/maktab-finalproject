package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Assistance;
import ir.maktab.finalproject.entity.Customer;
import ir.maktab.finalproject.entity.Specialist;
import ir.maktab.finalproject.exception.CustomerNotFoundException;
import ir.maktab.finalproject.exception.SpecialistNotFoundException;
import ir.maktab.finalproject.repository.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialistService extends BaseService<Specialist,Long>{

    @Autowired
    @Qualifier("specialistRepository")
    protected void setRepository(JpaRepository<Specialist, Long> repository) {
        this.repository = repository;
    }

    @Transactional
    public void changePassword(Specialist specialist , String password){
        specialist.setPassword(password);
        save(specialist);
    }

    @Transactional
    public void changePassword(Long specialistId , String password){
        Specialist specialist = findById(specialistId).orElseThrow(() -> new CustomerNotFoundException());
        specialist.setPassword(password);
        save(specialist);
    }

    @Transactional
    public void addAssistance(Specialist specialist , Assistance assistance){
        specialist.addAssistance(assistance);
        repository.save(specialist);
    }

    @Transactional
    public void addAssistanceById(Long specialistId , Assistance assistance){
        Optional<Specialist> retrieved = repository.findById(specialistId);
        Specialist specialist = retrieved.orElseThrow(() -> new SpecialistNotFoundException());
        specialist.addAssistance(assistance);
        repository.save(specialist);
    }

    @Transactional
    public void removeAssistance(Specialist specialist , Assistance assistance){
        specialist.removeAssistance(assistance);
        repository.save(specialist);
    }

    @Transactional
    public void removeAssistanceById(Long specialistId , Assistance assistance){
        Optional<Specialist> retrieved = repository.findById(specialistId);
        Specialist specialist = retrieved.orElseThrow(() -> new SpecialistNotFoundException());
        specialist.removeAssistance(assistance);
        repository.save(specialist);
    }

    @Transactional(readOnly = true)
    Page<Specialist> findByFirstName(String firstName, Pageable pageable){
        return ((SpecialistRepository)repository).findByFirstName(firstName, pageable);
    }

    @Transactional(readOnly = true)
    Page<Specialist> findByLastName(String lastName, Pageable pageable){
        return ((SpecialistRepository)repository).findByLastName(lastName,pageable);
    }

    @Transactional(readOnly = true)
    Page<Specialist> findByEmail(String email, Pageable pageable){
        return ((SpecialistRepository)repository).findByEmail(email, pageable);
    }

    @Transactional(readOnly = true)
    Page<Specialist> findByAssistanceId(Assistance assistance , Pageable pageable) {
        return ((SpecialistRepository) repository).findByAssistanceId(assistance, pageable);
    }

}
