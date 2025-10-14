package com.tanaka.template.util;

import com.tanaka.template.dto.AccountCreationNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Service
public class MailSenderService {
    private static final Logger logger = LoggerFactory.getLogger(MailSenderService.class);

    private final JavaMailSender mailSender;

    @Autowired
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a welcome email to a new admin/agent
     * @param to Email address of the recipient
     * @param name Name of the recipient
     * @param password Initial password for the account
     */
    public void sendWelcomeEmail(String to, String name, String password) {
        String subject = "Welcome to JoinAI Support Platform";
        String text = "Hello " + name + ",\n\n" +
                "Welcome to the JoinAI Support Platform. Your account has been created successfully.\n\n" +
                "Your login credentials:\n" +
                "Email: " + to + "\n" +
                "Password: " + password + "\n\n" +
                "Please change your password after the first login.\n\n" +
                "Best Regards,\nThe JoinAI Support Team";

        sendEmail(to, subject, text);
    }

    public void sendAccountCreationNotification(AccountCreationNotification notification) {
        String subject = "Account Creation Notification";
        String text = "Hello " + notification.getName() + ",\n\n" +
                "Your account has been sucessfully created with AGROLINK:\n\n" +

                "Please log in to the  platform to view the account profile and update it.\n\n" +
                "Happy Farming,\nThe AGROLINK Support Team";

        sendEmail(notification.getEmail(), subject, text);
    }





    private String formatTimestamp(Object timestamp) {
        try {
            if (timestamp instanceof LocalDateTime) {
                LocalDateTime dateTime = (LocalDateTime) timestamp;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
                return dateTime.format(formatter);
            } else if (timestamp instanceof String) {
                LocalDateTime dateTime = LocalDateTime.parse(timestamp.toString());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
                return dateTime.format(formatter);
            } else {
                return timestamp.toString();
            }
        } catch (Exception e) {
            logger.warn("Failed to format timestamp: {}", timestamp, e);
            return timestamp.toString();
        }
    }

    /**
     * Sends a password reset email with OTP
     * @param otp One-time password for resetting the password
     * @param email Email address of the recipient
     */
    public void sendPasswordResetEmail(String otp, String email) {
        String subject = "Password Reset Request - JoinAI Support Platform";
        String text = "Hello,\n\n" +
                "We received a request to reset your password for your JoinAI Support Platform account.\n\n" +
                "Your one-time password (OTP) for password reset is: " + otp + "\n\n" +
                "Please note that this OTP will expire in 15 minutes for security purposes.\n\n" +
                "If you did not request a password reset, please ignore this email or contact our support team immediately.\n\n" +
                "For your security, never share this OTP with anyone.\n\n" +
                "Best regards,\n" +
                "The JoinAI Support Team\n" +
                "support@joinai.com";

        sendEmail(email, subject, text);
    }

    /**
     * Validates if the provided string is a valid email address
     * @param email The email address to validate
     * @return true if the email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // RFC 5322 compliant email regex pattern
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Helper method to send emails
     * @param to Email address of the recipient
     * @param subject Subject of the email
     * @param text Body of the email
     */
    private void sendEmail(String to, String subject, String text) {
        // Validate email address before sending
        if (!isValidEmail(to)) {
            logger.error("Invalid email address: {}, email not sent", to);
            return; // Skip sending email to invalid addresses
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MailException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
