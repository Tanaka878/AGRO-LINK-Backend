package com.tanaka.template.service;

import com.tanaka.template.dto.AuthRequest;
import com.tanaka.template.dto.AuthenticationResponse;
import com.tanaka.template.dto.PasswordResetRequest;
import com.tanaka.template.dto.SignUpDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<AuthenticationResponse> createUser(SignUpDTO user);

    ResponseEntity<AuthenticationResponse> authenticateUser(AuthRequest authRequest);
    ResponseEntity<String> resetPassword(PasswordResetRequest request);
    ResponseEntity<String> initiatePasswordReset(String email);
}
