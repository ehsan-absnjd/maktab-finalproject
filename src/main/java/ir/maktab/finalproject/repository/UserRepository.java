package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Offer;
import ir.maktab.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepository extends JpaRepository<User,Long> , CustomerUserRepository {
    public User findByEmail(String username);
}
