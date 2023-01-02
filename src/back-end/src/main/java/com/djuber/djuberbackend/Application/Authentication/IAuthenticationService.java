package com.djuber.djuberbackend.Application.Authentication;

import com.djuber.djuberbackend.Controllers.Authentication.Request.LoginRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Request.SignUpRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoggedUserInfoResponse;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoginResponse;

public interface IAuthenticationService {

    LoggedUserInfoResponse getLoggedUserInfo(String email);

    Long signUpClient(SignUpRequest request);
}
