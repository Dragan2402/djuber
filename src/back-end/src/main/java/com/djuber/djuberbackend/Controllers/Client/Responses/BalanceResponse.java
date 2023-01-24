package com.djuber.djuberbackend.Controllers.Client.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalanceResponse {

    Double balance;

    public BalanceResponse() {
    }

    public BalanceResponse(Double balance) {
        this.balance = balance;
    }
}
