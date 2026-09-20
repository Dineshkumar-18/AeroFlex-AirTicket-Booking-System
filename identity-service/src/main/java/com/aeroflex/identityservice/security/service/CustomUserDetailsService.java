package com.aeroflex.identityservice.security.service;

import com.aeroflex.identityservice.security.principal.UserPrincipalFactory;
import com.aeroflex.identityservice.user.entity.User;
import com.aeroflex.identityservice.user.repository.UserRepository;
import com.aeroflex.identityservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPrincipalFactory userPrincipalFactory;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmailOrUsername(usernameOrEmail,usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Invalid username/email or password"));

        return userPrincipalFactory.create(user);
    }

}
