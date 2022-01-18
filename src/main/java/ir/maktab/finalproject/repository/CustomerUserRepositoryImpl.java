package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CustomerUserRepositoryImpl implements CustomerUserRepository{
    @PersistenceContext
    EntityManager entityManager;

    List<String> validParameters = Arrays.asList("firstName" , "lastName" , "email" , "role" , "assistance");

    @Override
    public List<User> findByParameters(Map<String, String[]> parameterMap) {
        if (!validParameters.stream().map(String::toLowerCase).collect(Collectors.toList())
                .containsAll(parameterMap.keySet().stream().map(String::toLowerCase).collect(Collectors.toList())) )
            throw new IllegalArgumentException("parameters are not valid.");
        String query = createQueryFromParameters(parameterMap);
        return entityManager.createQuery(query).getResultList();
    }

    private String createQueryFromParameters(Map<String, String[]> parameterMap){
        Map<String, String[]> cloned = new HashMap<>(parameterMap);
        String query = "SELECT u FROM User u";
        List<String> parameters=new ArrayList<>();
        if (cloned.containsKey("assistance")) {
            query = "SELECT u FROM Specialist u";
            String assistance = parameterMap.get("assistance")[0];
            parameters.add(assistance +" member of u.assistances");
            cloned.remove("assistance");
        }
        for (Map.Entry<String, String[]> entry : cloned.entrySet()){
            String parameter = validParameters.stream().filter(p->p.toLowerCase().equals(entry.getKey().toLowerCase())).findFirst().get();
            parameters.add("u." + parameter + "='" + entry.getValue()[0] +"'");
        }
        if (!parameters.isEmpty()){
            query+= " WHERE " + parameters.stream().collect(Collectors.joining(" AND "));
        }
        return query;
    }
}
