package com.tanaka.template.serviceImpl;

import com.tanaka.template.config.JWTService;
import com.tanaka.template.dto.AuthRequest;
import com.tanaka.template.dto.AuthenticationResponse;
import com.tanaka.template.dto.Role;
import com.tanaka.template.dto.SignUpDTO;
import com.tanaka.template.entity.Customer;
import com.tanaka.template.entity.User;
import com.tanaka.template.repository.UserRepository;
import com.tanaka.template.service.UserService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@Builder
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> createUser(SignUpDTO user) {

        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if (optionalUser.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        var newAccount = new Customer();
        newAccount.setEmail(user.getEmail());
        newAccount.setGender(user.getGender());
        newAccount.setFullname(user.getFullname());
        newAccount.setUsername(user.getUsername());
        newAccount.setPassword(passwordEncoder.encode(user.getPassword()));
        newAccount.setRole(Role.USER);

        userRepository.save(newAccount);

        String token = jwtService.generateToken(newAccount);

        var authResponse = new AuthenticationResponse();
        authResponse.setToken(token);
        authResponse.setUsername(user.getUsername());


        return ResponseEntity.ok().body(authResponse);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticateUser(AuthRequest authRequest) {

        var authenticationResponse = new AuthenticationResponse();



        Optional<User> byEmail = userRepository.findByEmail(authRequest.getEmail());

        if (byEmail.isPresent()){
            if (passwordEncoder.matches(authRequest.getPassword(), byEmail.get().getPassword())){
                String token = jwtService.generateToken(byEmail.get());
                authenticationResponse.setToken(token);
                authenticationResponse.setRole(byEmail.get().getRole());
                authenticationResponse.setUsername(authRequest.getEmail());

                return ResponseEntity.ok(authenticationResponse);
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();

    }


}
