package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.RequestStatus;
import ir.maktab.finalproject.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {
    @Autowired
    private EntityManager entityManager;

    List<String> validParameters = Arrays.asList("size"  ,"page" , "firstName" , "lastName" , "email" , "role" , "assistance");
    List<String> validReportParameters  = Arrays.asList("page" ,"size" , "before" , "after" ,
            "mindonecount" ,"maxdonecount" , "minreceivedcount" , "maxreceivedcount");

    @Override
    public List<User> getReport(Map<String, String> parameterMap) {
        if (!validReportParameters.containsAll(parameterMap.keySet()))
            throw new IllegalArgumentException();
        String queryString = "SELECT u FROM User u ";
        Set<String> queryParams = getParams(parameterMap);
        String conditions = queryParams.stream().collect(Collectors.joining(" AND "));
        if (queryParams.size()!=0)
            queryString+="WHERE " + conditions;
        System.out.println(queryString);
        Query query = entityManager.createQuery(queryString);
        try {
            setParams(query , parameterMap);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setPageable(query , parameterMap);
        return query.getResultList();
    }

    private void setParams(Query query, Map<String, String> parameterMap) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        if (parameterMap.containsKey("mindonecount")){
            query.setParameter("mindonecount" , Long.valueOf(parameterMap.get("mindonecount")));
        }
        if (parameterMap.containsKey("maxdonecount")){
            query.setParameter("maxdonecount" , Long.valueOf(parameterMap.get("maxdonecount")));
        }
        if (parameterMap.containsKey("minreceivedcount")){
            query.setParameter("minreceivedcount", Long.valueOf(parameterMap.get("minreceivedcount")));
        }
        if (parameterMap.containsKey("maxreceivedcount")){
            query.setParameter("maxreceivedcount", Long.valueOf(parameterMap.get("maxreceivedcount")));
        }
        if(parameterMap.containsKey("after")){
            query.setParameter("after" , formatter.parse(parameterMap.get("after")));
        }
        if(parameterMap.containsKey("before")){
            query.setParameter("before" , formatter.parse(parameterMap.get("before")));
        }
    }

    private Set<String> getParams(Map<String, String> parameterMap){
        Set<String> queryParams = new HashSet<>();
        if(parameterMap.containsKey("after")){
            queryParams.add("u.registrationDate >= :after");
        }
        if(parameterMap.containsKey("before")){
            queryParams.add("u.registrationDate <= :before");
        }
        if (parameterMap.containsKey("mindonecount")){
            queryParams.add("(SELECT COUNT(r) FROM Request r WHERE r.selectedOffer.specialist=u)>=:mindonecount");
        }
        if (parameterMap.containsKey("maxdonecount")){
            queryParams.add("(SELECT COUNT(r) FROM Request r WHERE r.selectedOffer.specialist=u)<=:maxdonecount");
        }
        if (parameterMap.containsKey("minreceivedcount")){
            queryParams.add("(SELECT COUNT(r) FROM Request r WHERE r.customer=u)>=:minreceivedcount");
        }
        if (parameterMap.containsKey("maxreceivedcount")){
            queryParams.add("(SELECT COUNT(r) FROM Request r WHERE r.customer=u)>=:maxreceivedcount");
        }
        return queryParams;
    }

    @Override
    public List<User> findByParameters(Map<String, String> parameterMap) {
        if (!validParameters.stream().map(String::toLowerCase).collect(Collectors.toList())
                .containsAll(parameterMap.keySet().stream().map(String::toLowerCase).collect(Collectors.toList())) )
            throw new IllegalArgumentException("parameters are not valid.");
        String queryString = createQueryFromParameters(parameterMap);
        Query query = entityManager.createQuery(queryString);
        setPageable(query , parameterMap);
        return query.getResultList();
    }

    private String createQueryFromParameters(Map<String, String> parameterMap){
        String query = "SELECT u FROM User u";
        List<String> parameters=new ArrayList<>();
        if (parameterMap.containsKey("assistance")) {
            query = "SELECT u FROM Specialist u";
            String assistance = parameterMap.get("assistance");
            parameters.add(assistance +" member of u.assistances");
        }
        for (Map.Entry<String, String> entry : parameterMap.entrySet()){
            if (entry.getKey().equals("page") ||entry.getKey().equals("size") || entry.getKey().equals("assistance") )
                continue;
            String parameter = validParameters.stream().filter(p->p.toLowerCase().equals(entry.getKey().toLowerCase())).findFirst().get();
            parameters.add("u." + parameter + "='" + entry.getValue() +"'");
        }
        if (!parameters.isEmpty()){
            query+= " WHERE " + parameters.stream().collect(Collectors.joining(" AND "));
        }
        return query;
    }

    private void setPageable(Query query , Map<String , String> parameterMap){
        if (parameterMap.containsKey("page")&& parameterMap.containsKey("size")){
            int page = Integer.parseInt(parameterMap.get("page"));
            int size = Integer.parseInt(parameterMap.get("size"));
            query.setFirstResult((page-1) * size);
            query.setMaxResults(page);
        }
    }
}
