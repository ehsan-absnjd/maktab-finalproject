package ir.maktab.finalproject;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@TestConfiguration
public class AuthTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User customer = new User("1", "password1", getCustomerAuthorities());
        User specialist = new User("2", "password2", getSpecialistAuthorities());
        User admin = new User("3", "password3", getAdminAuthorities());
        return new InMemoryUserDetailsManager(Arrays.asList(
                customer,specialist,admin
        ));
    }
    private List<GrantedAuthority> getAdminAuthorities() {
        String[] authorities = {"can_get_customers" , "can_get_specialists" ,"can_assign_assistance" ,
                "can_get_user_requests" , "can_get_users" , "can_get_report" , "can_get_requests_by_parameter",

        };
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getSpecialistAuthorities() {
        String[] authorities = {"can_add_offers"  };
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getCustomerAuthorities() {
        String[] authorities = {"can_add_request" , "can_evaluate" , "can_get_offers" , "can_select_offer" ,
                "can_pay_request"};
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }
}
