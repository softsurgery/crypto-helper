package com.zc.cryptohelper.crypto_helper.service;

import com.zc.cryptohelper.crypto_helper.config.JwtTokenProvider;
import com.zc.cryptohelper.crypto_helper.exceptions.auth.UserAlreadyExistsException;
import com.zc.cryptohelper.crypto_helper.models.Role;
import com.zc.cryptohelper.crypto_helper.models.User;
import com.zc.cryptohelper.crypto_helper.payload.JwtAuthenticationResponse;
import com.zc.cryptohelper.crypto_helper.payload.LoginRequest;
import com.zc.cryptohelper.crypto_helper.payload.SignupRequest;
import com.zc.cryptohelper.crypto_helper.repository.RoleRepository;
import com.zc.cryptohelper.crypto_helper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            return new JwtAuthenticationResponse(jwt);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password", e);
        }
    }

    public JwtAuthenticationResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Optional<Role> userRole = roleRepository.findByName("Standard");
        user.setRole(userRole.orElse(new Role()));
        userRepository.save(user);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signUpRequest.getUsername(),
                            signUpRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            return new JwtAuthenticationResponse(jwt);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to authenticate user after registration", e);
        }
    }
}
