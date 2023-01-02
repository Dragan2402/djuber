package com.djuber.djuberbackend.Controllers.Authentication;

import com.djuber.djuberbackend.Application.Authentication.IAuthenticationService;
import com.djuber.djuberbackend.Application.Authentication.Implementation.AuthenticationService;
import com.djuber.djuberbackend.Controllers.Authentication.Request.LoginRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Request.SignUpRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoggedUserInfoResponse;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoginResponse;
import com.djuber.djuberbackend.Infastructure.Security.JWTGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Tag(name="Authentication API",description = "Provides authentication logic.")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JWTGenerator jwtGenerator;

    private final IAuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<String> login(){
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }

    @GetMapping(value = "getLoggedUserInfo")
    public ResponseEntity<LoggedUserInfoResponse> getLoggedUserInfo(Principal user){
        return new ResponseEntity<>(authenticationService.getLoggedUserInfo(user.getName()),HttpStatus.OK);
    }

    @PostMapping(value = "signUp")
    public ResponseEntity<Long> signUp(@RequestBody @Valid SignUpRequest request){
        return new ResponseEntity<>(authenticationService.signUpClient(request),HttpStatus.CREATED);
    }
}
