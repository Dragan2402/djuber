package com.djuber.djuberbackend.Application.Services.Client;

import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Controllers.Client.Requests.UpdateClientRequest;

public interface IClientService {
    ClientResult getClientByEmail(String email);

    String getClientPictureByEmail(String email);

    void updateLoggedClientPicture(String email, String image);

    void updateLoggedClient(String email, UpdateClientRequest request);
}
