package com.library.orchestrator.security;

import com.library.orchestrator.entity.UserEntity;
import com.library.orchestrator.entity.UserRoleEntity;
import com.library.orchestrator.repository.UserRepository;
import com.library.orchestrator.repository.UserRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailsService(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found. user : " + username)
                );

        List<GrantedAuthority> authorities =
                userRoleRepository.findByUserId(user.getId())
                        .stream()
                        .map(UserRoleEntity::getRole)
                        .map(role -> new SimpleGrantedAuthority(
                                "ROLE_" + role.getName()
                        ))
                        .map(GrantedAuthority.class::cast)
                        .toList();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.isEnabled())
                .build();
    }
}
