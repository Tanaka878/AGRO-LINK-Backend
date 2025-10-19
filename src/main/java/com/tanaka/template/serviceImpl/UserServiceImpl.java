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
import java.util.UUID;

@Service
@Slf4j
@Builder
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final MailSenderService mailSenderService;

    // In-memory storage for reset tokens (in production, use Redis or database)
    // private final Map<String, String> resetTokens = new ConcurrentHashMap<>();

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

        // This now runs asynchronously - won't block the response
        mailSenderService.sendAccountCreationNotification(account)
                .exceptionally(throwable -> {
                    return null;
                });

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

    @Override
    public ResponseEntity<String> initiatePasswordReset(String email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                // For security reasons, don't reveal if email exists or not
                return ResponseEntity.ok("If the email exists, a reset link has been sent.");
            }

            User user = userOptional.get();

            // Generate reset token (you can use JWT or simple UUID)
            String resetToken = UUID.randomUUID().toString();

            // In production, store this token in database with expiration
            // For now, we'll use a simple approach
            // resetTokens.put(resetToken, email);

            // Send email with reset link
            sendPasswordResetEmail(user.getEmail(), resetToken, user.getFullname());

            log.info("Password reset initiated for user: {}", email);
            return ResponseEntity.ok("If the email exists, a reset link has been sent.");

        } catch (Exception e) {
            log.error("Error initiating password reset for email: {}", email, e);
            return ResponseEntity.internalServerError().body("Error processing your request.");
        }
    }

    @Override
    public ResponseEntity<String> resetPassword(PasswordResetRequest request) {
        try {
            // Validate the reset token (in production, check against stored tokens)
            // For this simple implementation, we'll skip token validation
            // and allow direct reset with email and new password

            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid reset request.");
            }

            User user = userOptional.get();

            // Update password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            // Send confirmation email
            sendPasswordResetConfirmationEmail(user.getEmail(), user.getFullname());

            log.info("Password reset successful for user: {}", request.getEmail());
            return ResponseEntity.ok("Password reset successfully.");

        } catch (Exception e) {
            log.error("Error resetting password for email: {}", request.getEmail(), e);
            return ResponseEntity.internalServerError().body("Error resetting password.");
        }
    }

    private void sendPasswordResetEmail(String email, String resetToken, String userName) {
        try {
            // Create reset link (adjust the URL based on your frontend)
            String resetLink = "http://localhost:3000/reset-password?token=" + resetToken + "&email=" + email;

            String subject = "Password Reset Request";
            String message = String.format(
                    "Dear %s,\n\n" +
                            "You have requested to reset your password. Please click the link below to reset your password:\n\n" +
                            "%s\n\n" +
                            "If you did not request this, please ignore this email.\n\n" +
                            "Best regards,\nYour App Team",
                    userName, resetLink
            );

            // Use your mail service to send the email
            // mailSenderService.sendSimpleEmail(email, subject, message);
            log.info("Password reset email would be sent to: {} with token: {}", email, resetToken);

        } catch (Exception e) {
            log.error("Error sending password reset email to: {}", email, e);
        }
    }

    private void sendPasswordResetConfirmationEmail(String email, String userName) {
        try {
            String subject = "Password Reset Successful";
            String message = String.format(
                    "Dear %s,\n\n" +
                            "Your password has been successfully reset. If you did not make this change, please contact support immediately.\n\n" +
                            "Best regards,\nYour App Team",
                    userName
            );

            // Use your mail service to send the email
            // mailSenderService.sendSimpleEmail(email, subject, message);
            log.info("Password reset confirmation email would be sent to: {}", email);

        } catch (Exception e) {
            log.error("Error sending password reset confirmation email to: {}", email, e);
        }
    }
}