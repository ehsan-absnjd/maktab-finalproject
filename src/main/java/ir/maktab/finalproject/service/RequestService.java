package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

public class RequestService extends BaseService<Request,Long>{

    @Autowired
    @Qualifier("requestRepository")
    protected void setRepository(JpaRepository<Request, Long> repository) {
        this.repository = repository;
    }
}
