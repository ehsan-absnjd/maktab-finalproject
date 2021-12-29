package ir.maktab.finalproject.service;

import ir.maktab.finalproject.exception.DuplicateTitleException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public abstract class BaseService <T , ID extends  Number>{

    protected JpaRepository<T,ID> repository ;

    protected abstract void setRepository(JpaRepository<T,ID> repository);

    @Transactional
    public T save(T entity){
        try{
            return repository.save(entity);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateTitleException();
        }
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(ID id){
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<T> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    @Transactional
    public void delete(T entity){
        repository.delete(entity);
    }

    @Transactional
    public void deleteById(ID id){
        repository.deleteById(id);
    }
}
