package com.djuber.djuberbackend.Application.Services.Admin.Results;


import com.djuber.djuberbackend.Domain.Admin.Admin;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor
public class AdminResult {

    Long id;

    Long identityId;

    String email;

    String firstName;

    String lastName;

    String city;

    String phoneNumber;

    public AdminResult(@NotNull Admin admin){
        this.id = admin.getId();
        this.identityId = admin.getIdentity().getId();
        this.email = admin.getIdentity().getEmail();
        this.firstName = admin.getFirstName();
        this.lastName = admin.getLastName();
        this.city = admin.getCity();
        this.phoneNumber = admin.getPhoneNumber();
    }
}
