package ir.maktab.finalproject.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class BaseService <T , ID extends  Number>{

    protected JpaRepository<T,ID> repository ;

    protected abstract void setRepository(JpaRepository<T,ID> repository);

    @Transactional
    public void deleteById(ID id){
        repository.deleteById(id);
    }
}
