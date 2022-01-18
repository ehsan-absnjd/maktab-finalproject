package ir.maktab.finalproject.service;

import ir.maktab.finalproject.entity.Role;
import ir.maktab.finalproject.entity.User;
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
        List<GrantedAuthority> authorities = getAuthorities(user.getRole());
        return new org.springframework.security.core.userdetails.User(String.valueOf( user.getId()), user.getPassword(),authorities );
    }

    private List<GrantedAuthority> getAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (role){
            case "CUSTOMER":
                authorities= getCustomerAuthorities();
                break;
            case "SPECIALIST":
                authorities= getSpecialistAuthorities();
                break;
            case "ADMIN":
                authorities= getAdminAuthorities();
        }
        return authorities;
    }

    private List<GrantedAuthority> getAdminAuthorities() {
        String[] authorities = {"canaddassistance","can_read" , "can_write"};
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getSpecialistAuthorities() {
        String[] authorities = {"can_read" , "can_write"};
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getCustomerAuthorities() {
        String[] authorities = {"can_read" , "can_write"};
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }
}
