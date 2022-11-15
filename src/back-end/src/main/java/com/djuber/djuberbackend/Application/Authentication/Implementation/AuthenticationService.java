package com.djuber.djuberbackend.Application.Authentication.Implementation;

import com.djuber.djuberbackend.Application.Authentication.IAuthenticationService;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService implements IAuthenticationService, UserDetailsService {

    final IIdentityRepository identityRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null) throw new UsernameNotFoundException("User does not exist");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        identity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(identity.getEmail(), identity.getPassword(), authorities);
    }


}
