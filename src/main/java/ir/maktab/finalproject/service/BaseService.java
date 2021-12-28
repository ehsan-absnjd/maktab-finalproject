package ir.maktab.finalproject.service;

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
    public void save(T entity){
        repository.save(entity);
    }

    public Optional<T> findById(ID id){
        return repository.findById(id);
    }

    public List<T> findAll(){
        return repository.findAll();
    }

    public Page<T> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public void delete(T entity){
        repository.delete(entity);
    }

    public void deleteById(ID id){
        repository.deleteById(id);
    }
}
