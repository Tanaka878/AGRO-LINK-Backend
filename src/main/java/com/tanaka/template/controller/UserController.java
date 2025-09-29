package com.tanaka.template.controller;

import com.tanaka.template.dto.AuthRequest;
import com.tanaka.template.dto.AuthenticationResponse;
import com.tanaka.template.dto.SignUpDTO;
import com.tanaka.template.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody SignUpDTO user){
        return userService.createUser(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthRequest authRequest){
        return userService.authenticateUser(authRequest);
    }

}
