package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Request;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface CustomRequestRepository {
    public List<Request> findByParameterMap(Map<String,String> parameterMap);
}
