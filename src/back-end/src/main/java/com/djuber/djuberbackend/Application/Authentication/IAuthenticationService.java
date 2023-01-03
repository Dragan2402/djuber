package com.djuber.djuberbackend.Application.Authentication;

import com.djuber.djuberbackend.Controllers.Authentication.Request.LoginRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Request.PasswordResetRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Request.SignUpRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Request.SocialUserRequest;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoggedUserInfoResponse;
import com.djuber.djuberbackend.Controllers.Authentication.Responses.LoginResponse;

public interface IAuthenticationService {

    LoggedUserInfoResponse getLoggedUserInfo(String email);

    Long signUpClient(SignUpRequest request);

    String socialSignIn(SocialUserRequest request);

    String signUpSocialClient(SocialUserRequest request);

    void verifyClientAccount(String token);

    void resetPassword(PasswordResetRequest request);

    void sendPasswordResetToken(String email);
}
