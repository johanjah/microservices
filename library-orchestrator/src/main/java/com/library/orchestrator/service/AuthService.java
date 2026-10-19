package com.library.orchestrator.service;

import com.library.orchestrator.dto.RegisterRequest;
import com.library.orchestrator.entity.RoleEntity;
import com.library.orchestrator.entity.UserEntity;
import com.library.orchestrator.entity.UserRoleEntity;
import com.library.orchestrator.repository.RoleRepository;
import com.library.orchestrator.repository.UserRepository;
import com.library.orchestrator.repository.UserRoleRepository;
import com.library.orchestrator.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public Long register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already used");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already used");
        }

        RoleEntity memberRole = roleRepository.findByName("MEMBER")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Role MEMBER not exists in database"
                        )
                );

        UserEntity user = new UserEntity();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        UserEntity savedUser = userRepository.save(user);

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUser(savedUser);
        userRole.setRole(memberRole);

        userRoleRepository.save(userRole);

        return savedUser.getId();
    }


    @Transactional(readOnly = true)
    public String login(String username, String rawPassword) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BadCredentialsException("Username atau password salah")
                );

        boolean passwordMatches = passwordEncoder.matches(
                rawPassword,
                user.getPassword()
        );

        if (!passwordMatches || !user.isEnabled()) {
            throw new BadCredentialsException(
                    "Username atau password salah"
            );
        }

        return jwtService.generateToken(user.getUsername());
    }

}
