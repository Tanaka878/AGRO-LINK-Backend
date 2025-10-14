package com.tanaka.template.serviceImpl;

import com.tanaka.template.config.JWTService;
import com.tanaka.template.dto.*;
import com.tanaka.template.entity.Customer;
import com.tanaka.template.entity.User;
import com.tanaka.template.repository.UserRepository;
import com.tanaka.template.service.UserService;
import com.tanaka.template.util.MailSenderService;
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
    private final MailSenderService mailSenderService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mailSenderService = mailSenderService;
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

        var account = new AccountCreationNotification();
        account.setRole(user.getRole());
        account.setName(user.getFullname());
        account.setEmail(user.getEmail());

        mailSenderService.sendAccountCreationNotification(account);


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
                authenticationResponse.setName(byEmail.get().getFullname());

                return ResponseEntity.ok(authenticationResponse);
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();

    }


}
