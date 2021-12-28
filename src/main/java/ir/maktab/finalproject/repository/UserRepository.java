package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
