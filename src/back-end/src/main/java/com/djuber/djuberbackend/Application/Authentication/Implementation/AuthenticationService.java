package com.djuber.djuberbackend.Application.Authentication.Implementation;

import com.djuber.djuberbackend.Application.Authentication.IAuthenticationService;
import com.djuber.djuberbackend.Controllers.Authentication.Request.SignUpRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoggedUserInfoResponse;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.Role;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.EmailAlreadyExistsException;
import com.djuber.djuberbackend.Infastructure.Repositories.Admin.IAdminRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.RoleRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService implements IAuthenticationService, UserDetailsService {

    final IIdentityRepository identityRepository;

    final IAdminRepository adminRepository;

    final IClientRepository clientRepository;

    final IDriverRepository driverRepository;

    final PasswordEncoder passwordEncoder;

    final RoleRepository roleRepository;

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


    @Override
    public LoggedUserInfoResponse getLoggedUserInfo(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null) throw new UsernameNotFoundException("User does not exist");
        LoggedUserInfoResponse loggedUserInfoResponse = new LoggedUserInfoResponse();
        loggedUserInfoResponse.setEmail(identity.getEmail());
        switch (identity.getUserType()){
            case ADMIN -> {
                Admin admin = adminRepository.findByIdentityId(identity.getId());
                loggedUserInfoResponse.setFirstName(admin.getFirstName());
                loggedUserInfoResponse.setLastName(admin.getLastName());
            }
            case CLIENT -> {
                Client client = clientRepository.findByIdentityId(identity.getId());
                loggedUserInfoResponse.setFirstName(client.getFirstName());
                loggedUserInfoResponse.setLastName(client.getLastName());
            }
            case DRIVER -> {
                Driver driver = driverRepository.findByIdentityId(identity.getId());
                loggedUserInfoResponse.setFirstName(driver.getFirstName());
                loggedUserInfoResponse.setLastName(driver.getLastName());
            }
            default -> {
                throw new UsernameNotFoundException("User does not exist");
            }
        }
        return loggedUserInfoResponse;
    }

    @Override
    public Long signUpClient(SignUpRequest request) throws EmailAlreadyExistsException {
        Identity identity = identityRepository.findByEmail(request.getEmail());
        if(identity != null) throw new EmailAlreadyExistsException("User with provided email already exists");
        Identity identityToSave = new Identity();

        identityToSave.setEmail(request.getEmail());
        identityToSave.setPassword(passwordEncoder.encode(request.getPassword()));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_CLIENT"));
        identityToSave.setRoles(roles);
        identityToSave.setDeleted(false);
        identityToSave.setUserType(UserType.CLIENT);

        Identity identitySaved = identityRepository.save(identityToSave);

        Client clientToSave = new Client();
        clientToSave.setFirstName(request.getFirstName());
        clientToSave.setLastName(request.getLastName());
        clientToSave.setCity(request.getCity());
        clientToSave.setPhoneNumber(request.getPhoneNumber());
        clientToSave.setVerified(true);
        clientToSave.setDeleted(false);
        clientToSave.setInRide(false);
        clientToSave.setBlocked(false);
        clientToSave.setPicture("TO-DO");
        clientToSave.setIdentity(identitySaved);
        Client saved = clientRepository.save(clientToSave);

        return saved.getId();
    }
}
