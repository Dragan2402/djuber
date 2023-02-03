package com.djuber.djuberbackend.Application.Services.Client.Results;

import com.djuber.djuberbackend.Domain.Admin.Admin;
import com.djuber.djuberbackend.Domain.Client.Client;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor
public class ClientResult {

    Long id;

    Long identityId;

    String email;

    String firstName;

    String lastName;

    String city;

    String phoneNumber;

    String note;

    boolean blocked;

    public ClientResult(@NotNull Client client){
        this.id = client.getId();
        this.identityId = client.getIdentity().getId();
        this.email = client.getIdentity().getEmail();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.city = client.getCity();
        this.phoneNumber = client.getPhoneNumber();
        this.note = client.getNote();
        this.blocked = client.getBlocked();
    }
}
