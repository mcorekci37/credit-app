package com.emce.authserver.service;

import com.emce.authserver.entity.UserCredential;
import com.emce.authserver.exception.UserNotFoundException;
import com.emce.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    public static final String USER_NOT_FOUND_MSG = "User not found with email %s";
    public static final String USER_ID_NOT_FOUND_MSG = "User not found with id %s";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }
    public UserCredential loadUserById(Integer id) throws UsernameNotFoundException {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND_MSG, id)));
    }
    public boolean checkExistsById(Integer id) {
        return userRepository.existsById(id);
    }

}
