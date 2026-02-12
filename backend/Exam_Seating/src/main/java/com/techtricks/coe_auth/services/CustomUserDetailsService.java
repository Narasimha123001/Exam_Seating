package com.techtricks.coe_auth.services;


import com.techtricks.coe_auth.repositorys.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: "));
    }


//    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
//        return userDetailsRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Email not found: "+ email));
//    }
}
