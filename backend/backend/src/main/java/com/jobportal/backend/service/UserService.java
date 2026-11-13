package com.jobportal.backend.service;

import com.jobportal.backend.entity.User;
import com.jobportal.backend.repository.UserRepository;
import com.jobportal.backend.dto.RegisterRequest;
import com.jobportal.backend.dto.LoginRequest;

import org.springframework.stereotype.Service;

import java.util.List;
import java.security.SecureRandom;
import java.security.MessageDigest;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String role = request.getRole().toUpperCase();

        if (!role.equals("SEEKER") && !role.equals("RECRUITER")) {
            throw new RuntimeException("Invalid user role");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(hashPassword(request.getPassword()));
        user.setRole(role);

        return repository.save(user);
    }

    public User login(LoginRequest request) {

    User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    String storedPassword = user.getPasswordHash();
    String enteredPassword = request.getPassword();

    boolean passwordMatches;

    // Handle old accounts whose passwords are still plain text
    if (storedPassword != null && !storedPassword.contains(":")) {

        passwordMatches = MessageDigest.isEqual(
            enteredPassword.getBytes(java.nio.charset.StandardCharsets.UTF_8),
            storedPassword.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );

        // Upgrade the old password to a secure hash
        if (passwordMatches) {
            user.setPasswordHash(hashPassword(enteredPassword));
            repository.save(user);
        }

    } else {
        // Verify passwords that are already hashed
        passwordMatches = verifyPassword(enteredPassword, storedPassword);
    }

    if (!passwordMatches) {
        throw new RuntimeException("Invalid password");
    }

    return user;
}

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String hashPassword(String password) {

        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                120000,
                256
            );

            SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            spec.clearPassword();

            return Base64.getEncoder().encodeToString(salt)
                    + ":"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed");
        }
    }

    private boolean verifyPassword(String password, String storedHash) {

        try {
            String[] parts = storedHash.split(":");

            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] originalHash = Base64.getDecoder().decode(parts[1]);

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                120000,
                256
            );

            SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] newHash = factory.generateSecret(spec).getEncoded();

            spec.clearPassword();

            return MessageDigest.isEqual(originalHash, newHash);

        } catch (Exception e) {
            return false;
        }
    }
}