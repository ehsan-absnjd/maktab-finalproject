package ir.maktab.finalproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("")
public class TestController {
    @Value("${secret}")
    String secret;

    @Value("${tokenexpireperiod}")
    String tokenPeriod;

    @Value("${refreshtokenexpireperiod}")
    String refreshTokenPeriod;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    Algorithm algorithm;
    @PostConstruct
    public void init(){
        algorithm = Algorithm.HMAC256(secret.getBytes());
    }

    @PreAuthorize("hasAuthority('can_read')")
    @GetMapping("test")
    public String test(){
        return "ok";
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginInputDTO loginInputDTO ){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginInputDTO.getEmail(), loginInputDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetails user = (UserDetails) authenticate.getPrincipal();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", getAccessToken(user));
        tokens.put("refresh_token", getRefreshToken(user));
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/api/auth/refreshtoken")
    public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                UserDetails user = userDetailsService.loadUserByUsername(email);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", getAccessToken(user));
                return ResponseEntity.ok(tokens);
            }catch (RuntimeException exception) {
                throw exception;
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    public String getRefreshToken(UserDetails user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(refreshTokenPeriod) * 60 * 1000))
                .sign(algorithm);
    }

    public String getAccessToken(UserDetails user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(tokenPeriod) * 60 * 1000))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }
}
