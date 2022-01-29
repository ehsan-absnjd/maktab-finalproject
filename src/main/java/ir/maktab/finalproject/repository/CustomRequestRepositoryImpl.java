package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.Request;
import ir.maktab.finalproject.entity.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class CustomRequestRepositoryImpl implements CustomRequestRepository {
    @PersistenceContext
    private EntityManager entityManager;

    List<String> validParameters = Arrays.asList("page" ,"size" , "before" , "after" , "requeststatus" , "assistance" , "subassistance");

    @Override
    public List<Request> findByParameterMap(Map<String, String> parameterMap) {
        if (!validParameters.containsAll(parameterMap.keySet()))
            throw new IllegalArgumentException();
        String queryString = "SELECT r FROM Request r ";
        Set<String> queryParams = getParams(parameterMap);
        String conditions = queryParams.stream().collect(Collectors.joining(" AND "));
        if (queryParams.size()!=0)
            queryString+="WHERE " + conditions;
        Query query = entityManager.createQuery(queryString);
        try {
            setParams(query , parameterMap);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return query.getResultList();
    }

    private void setParams(Query query, Map<String, String> parameterMap) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        if(parameterMap.containsKey("after")){
            query.setParameter("after" , formatter.parse(parameterMap.get("after")));
        }
        if(parameterMap.containsKey("before")){
            query.setParameter("before" , formatter.parse(parameterMap.get("before")));
        }
        if (parameterMap.containsKey("requeststatus")){
            query.setParameter("status" , RequestStatus.valueOf(parameterMap.get("requeststatus")));
        }
        if (parameterMap.containsKey("subassistance")){
            query.setParameter("subid", Long.valueOf(parameterMap.get("subassistance")));
        }
        if(parameterMap.containsKey("assistance")){
            query.setParameter("id", Long.valueOf(parameterMap.get("assistance")));
        }
        if (parameterMap.containsKey("page") && parameterMap.containsKey("size")){
            int page = Integer.parseInt(parameterMap.get("page"));
            int size = Integer.parseInt(parameterMap.get("size"));
            query.setFirstResult((page-1) * size);
            query.setMaxResults(page);
        }
    }

    private Set<String> getParams(Map<String, String> parameterMap){
        Set<String> queryParams = new HashSet<>();
        if(parameterMap.containsKey("after")){
            queryParams.add("r.registerDate >= :after");
        }
        if(parameterMap.containsKey("before")){
            queryParams.add("r.registerDate <= :before");
        }
        if (parameterMap.containsKey("requeststatus")){
            queryParams.add("r.status =:status");
        }
        if (parameterMap.containsKey("subassistance")){
            queryParams.add("r.subAssistance.id =:subid");
        }
        if(parameterMap.containsKey("assistance")){
            queryParams.add("r.subAssistance.assistance.id =:id");
        }
        return queryParams;
    }
}