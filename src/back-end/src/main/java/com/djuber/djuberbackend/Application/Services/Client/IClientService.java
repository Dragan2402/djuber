package com.djuber.djuberbackend.Application.Services.Client;

import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Controllers.Client.Requests.UpdateClientRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClientService {
    ClientResult getClientByEmail(String email);

    String getClientPictureByEmail(String email);

    void updateLoggedClientPicture(String email, String image);

    void updateLoggedClient(String email, UpdateClientRequest request);

    Page<ClientResult> readPageable(Pageable pageable);

    Page<ClientResult> readPageableWithFilter(Pageable pageable, String filter);

    void blockClient(long clientId);

    void unblockClient(long clientId);

    void updateClientNote(long clientId, String note);

    String getClientNote(long clientId);

    Double getClientBalanceByEmail(String email);

    void addLoggedClientFunds(String email, Double amount);
}
