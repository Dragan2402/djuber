package com.djuber.djuberbackend.Application.Services.Authentication.Implementation;

import com.djuber.djuberbackend.Application.Services.Authentication.IAuthenticationService;
import com.djuber.djuberbackend.Controllers.Authentication.Request.PasswordChangeRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Request.PasswordResetRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Request.SignUpRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Request.SocialUserRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoggedUserInfoResponse;
import com.djuber.djuberbackend.Controllers._Common.Responses.IdResponse;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.Role;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Client.ClientSigningType;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.*;
import com.djuber.djuberbackend.Infastructure.Repositories.Admin.IAdminRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.RoleRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Util.DateCalculator;
import com.djuber.djuberbackend.Infastructure.Util.EmailSenderService;
import com.djuber.djuberbackend.Infastructure.Util.MediaService;
import com.djuber.djuberbackend.Infastructure.Util.RandomStringGenerator;
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

    final RandomStringGenerator randomStringGenerator;

    final DateCalculator dateCalculator;

    final EmailSenderService emailSenderService;

    final MediaService mediaService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null) throw new UsernameNotFoundException("User does not exist");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        identity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(identity.getEmail(), identity.getPassword(), isAccountVerified(identity), true,true, true ,authorities);
    }

    private boolean isAccountVerified(Identity identity){
        if(identity.getUserType()!=UserType.CLIENT){
            return true;
        }
        Client client = clientRepository.findByIdentityId(identity.getId());
        return client.getVerified();
    }

    @Override
    public LoggedUserInfoResponse getLoggedUserInfo(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null) throw new UsernameNotFoundException("User does not exist");
        LoggedUserInfoResponse loggedUserInfoResponse = new LoggedUserInfoResponse();
        loggedUserInfoResponse.setEmail(identity.getEmail());
        String[] roleParts=identity.getRoles().get(0).getName().split("_");
        loggedUserInfoResponse.setRole(roleParts[1]);
        switch (identity.getUserType()){
            case ADMIN -> {
                Admin admin = adminRepository.findByIdentityId(identity.getId());
                loggedUserInfoResponse.setFirstName(admin.getFirstName());
                loggedUserInfoResponse.setLastName(admin.getLastName());
                loggedUserInfoResponse.setPicture(mediaService.readUserPictureAsBase64String(admin.getId(),UserType.ADMIN));
            }
            case CLIENT -> {
                Client client = clientRepository.findByIdentityId(identity.getId());
                loggedUserInfoResponse.setFirstName(client.getFirstName());
                loggedUserInfoResponse.setLastName(client.getLastName());
                loggedUserInfoResponse.setPicture(mediaService.readUserPictureAsBase64String(client.getId(),UserType.CLIENT));
            }
            case DRIVER -> {
                Driver driver = driverRepository.findByIdentityId(identity.getId());
                loggedUserInfoResponse.setFirstName(driver.getFirstName());
                loggedUserInfoResponse.setLastName(driver.getLastName());
                loggedUserInfoResponse.setPicture(mediaService.readUserPictureAsBase64String(driver.getId(),UserType.DRIVER));
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
        clientToSave.setVerified(false);
        clientToSave.setClientSigningType(ClientSigningType.DEFAULT);
        clientToSave.setDeleted(false);
        clientToSave.setInRide(false);
        clientToSave.setBalance(0D);
        clientToSave.setBlocked(false);
        clientToSave.setIdentity(identitySaved);
        String verificationToken = randomStringGenerator.generate(50);
        clientToSave.setVerificationToken(verificationToken);
        clientToSave.setVerificationTokenExpirationDate(dateCalculator.getDate24HoursFromNow());
        Client saved = clientRepository.save(clientToSave);


        if(request.getPicture()!=null){
            mediaService.saveBase64AsPicture(saved.getId(),identitySaved.getUserType(),request.getPicture());
        }else{
            mediaService.setUserDefaultPicture(saved.getId(),identitySaved.getUserType());
        }

        emailSenderService.sendClientVerificationEmail(identitySaved.getEmail(),verificationToken);

        return saved.getId();
    }


    @Override
    public String socialSignIn(SocialUserRequest request) {
        Identity identity = identityRepository.findByEmail(request.getEmail());
        if(identity == null){
            return this.signUpSocialClient(request);
        }
        Client client = clientRepository.findByIdentityId(identity.getId());
        if(client.getClientSigningType() != this.getClientSigningType(request.getProvider())){
            throw new DifferentSocialSigningProvidersException("Not same provider as one that is already registered with provided email address.");
        }
        return identity.getEmail();
    }

    @Override
    public String signUpSocialClient(SocialUserRequest request) {
        Identity identityToSave = new Identity();

        identityToSave.setEmail(request.getEmail());
        identityToSave.setPassword(passwordEncoder.encode(request.getId()));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_CLIENT"));
        identityToSave.setRoles(roles);
        identityToSave.setDeleted(false);
        identityToSave.setUserType(UserType.CLIENT);

        Identity identitySaved = identityRepository.save(identityToSave);

        Client clientToSave = new Client();
        clientToSave.setFirstName(request.getFirstName());
        clientToSave.setLastName(request.getLastName());
        clientToSave.setCity("Novi Sad");
        clientToSave.setPhoneNumber("");
        clientToSave.setVerified(true);
        clientToSave.setClientSigningType(this.getClientSigningType(request.getProvider()));
        clientToSave.setDeleted(false);
        clientToSave.setBalance(0D);
        clientToSave.setInRide(false);
        clientToSave.setBlocked(false);
        clientToSave.setIdentity(identitySaved);

        Client saved = clientRepository.save(clientToSave);
        mediaService.setUserDefaultPicture(saved.getId(),UserType.CLIENT);
        return identitySaved.getEmail();
    }

    @Override
    public void verifyClientAccount(String token) {
        Client client = clientRepository.findByVerificationToken(token);
        if(client == null){
            throw new InvalidTokenException("Provided verification token is invalid");
        }
        if(dateCalculator.isDateInThePast(client.getVerificationTokenExpirationDate())){
            throw new TokenExpiredException("Verification token is expired");
        }
        client.setVerified(true);
        clientRepository.save(client);

    }

    @Override
    public void resetPassword(PasswordResetRequest request) {
        Identity identity = identityRepository.findIdentityByPasswordResetToken(request.getToken());
        if(identity == null){
            throw new InvalidTokenException("Provided password reset token is invalid");
        }
        if(dateCalculator.isDateInThePast(identity.getPasswordResetTokenExpirationDate())){
            throw new TokenExpiredException("Password reset token is expired");
        }
        identity.setPassword(passwordEncoder.encode(request.getPassword()));
        identityRepository.save(identity);
    }

    @Override
    public void sendPasswordResetToken(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("User with that mail does not exist");
        }
        String token = randomStringGenerator.generate(50);
        identity.setPasswordResetToken(token);
        identity.setPasswordResetTokenExpirationDate(dateCalculator.getDate24HoursFromNow());

        identityRepository.save(identity);

        emailSenderService.sendPasswordResetEmail(email, token);
    }

    @Override
    public void updateLoggedUserPassword(String email, PasswordChangeRequest request) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("User with that mail does not exist");
        }
        identity.setPassword(passwordEncoder.encode(request.getPassword()));
        identityRepository.save(identity);
    }

    @Override
    public Long getLoggedUserIdentityId(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("User with that mail does not exist");
        }
        return identity.getId();
    }

    private ClientSigningType getClientSigningType(String type){
        try {
            return ClientSigningType.valueOf(type);
        }catch (Exception ex){
            throw new UnsupportedSocialProviderExcetpion("The provided social provider is not supported.");
        }
    }
}
