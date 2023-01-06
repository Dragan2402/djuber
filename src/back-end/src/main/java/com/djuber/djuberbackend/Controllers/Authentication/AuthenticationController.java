package com.djuber.djuberbackend.Controllers.Authentication;

import com.djuber.djuberbackend.Application.Authentication.IAuthenticationService;
import com.djuber.djuberbackend.Application.Authentication.Implementation.AuthenticationService;
import com.djuber.djuberbackend.Controllers.Authentication.Request.*;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoggedUserInfoResponse;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoginResponse;
import com.djuber.djuberbackend.Infastructure.Security.JWTGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Date;
import java.util.Map;

@Tag(name="Authentication API",description = "Provides authentication logic.")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JWTGenerator jwtGenerator;

    private final IAuthenticationService authenticationService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(new LoginResponse(jwtGenerator.generateToken(authentication)), HttpStatus.OK);
    }

    @GetMapping(value = "refreshToken")
    public ResponseEntity<LoginResponse> refreshJwtToken(Principal user){
        return new ResponseEntity<>(new LoginResponse(jwtGenerator.generateRefreshToken(user.getName())), HttpStatus.OK);
    }

    @GetMapping(value = "getLoggedUserInfo")
    public ResponseEntity<LoggedUserInfoResponse> getLoggedUserInfo(Principal user){
        return new ResponseEntity<>(authenticationService.getLoggedUserInfo(user.getName()),HttpStatus.OK);
    }

    @PostMapping(value = "signUp")
    public ResponseEntity<Long> signUp(@RequestBody @Valid SignUpRequest request){
        return new ResponseEntity<>(authenticationService.signUpClient(request),HttpStatus.CREATED);
    }

    @PostMapping(value = "socialSignIn")
    public ResponseEntity<LoginResponse> socialSignIn(@RequestBody SocialUserRequest request){
        return new ResponseEntity<>(new LoginResponse(jwtGenerator.generateRefreshToken(authenticationService.socialSignIn(request))),HttpStatus.OK);
    }

    @PutMapping(value = "verifyClientAccount")
    public void verify(@RequestBody VerifyClientAccountRequest request){
        authenticationService.verifyClientAccount(request.getToken());
    }

    @PutMapping(value = "passwordReset")
    public void passwordReset(@RequestBody @Valid PasswordResetRequest request){
        authenticationService.resetPassword(request);
    }

    @PutMapping(value = "passwordChange")
    public void passwordChange(Principal user, @RequestBody @Valid PasswordChangeRequest request){
        authenticationService.updateLoggedUserPassword(user.getName() ,request);
    }

    @GetMapping(value = "passwordResetToken")
    public void getPasswordResetToken(@RequestParam String email){
        authenticationService.sendPasswordResetToken(email);
    }


}
