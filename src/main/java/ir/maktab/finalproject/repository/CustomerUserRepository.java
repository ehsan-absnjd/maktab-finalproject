package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.User;

import java.util.List;
import java.util.Map;

public interface CustomerUserRepository {
    public List<User> findByParameters(Map<String, String[]> parameterMap);
}
