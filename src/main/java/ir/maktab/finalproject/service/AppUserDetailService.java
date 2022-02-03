package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Role;
import ir.maktab.finalproject.entity.User;
import ir.maktab.finalproject.entity.UserStatus;
import ir.maktab.finalproject.exception.UserNotFoundException;
import ir.maktab.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if (user==null)
            throw new UserNotFoundException();
        List<GrantedAuthority> authorities = getAuthorities(user);
        return new org.springframework.security.core.userdetails.User(String.valueOf( user.getId()), user.getPassword(),authorities );
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        String role = user.getRole();
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (role){
            case "CUSTOMER":
                authorities= getCustomerAuthorities(user);
                break;
            case "SPECIALIST":
                authorities= getSpecialistAuthorities(user);
                break;
            case "ADMIN":
                authorities= getAdminAuthorities(user);
        }
        return authorities;
    }

    private List<GrantedAuthority> getAdminAuthorities(User user) {
        String[] authorities = {"can_get_customers" , "can_get_specialists" ,"can_assign_assistance" ,
                "can_get_user_requests" , "can_add_assistance" , "can_add_subassistance", "can_get_users" ,
                "can_get_report" , "can_get_requests_by_parameter", "can_approve_specialists"
        };
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getSpecialistAuthorities(User user) {
        String[] authorities = {"can_add_offers" , "can_upload_photo" , "can_start_request" , "can_end_request" };
        if (user.getStatus()!= UserStatus.APPROVED)
            authorities=new String[]{"can_upload_photo"};
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getCustomerAuthorities(User user) {
        String[] authorities = {"can_add_request" , "can_evaluate" , "can_get_offers" , "can_select_offer" ,
        "can_pay_request"};
        if (user.getStatus()!= UserStatus.APPROVED)
            authorities=new String[]{};
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }
}
