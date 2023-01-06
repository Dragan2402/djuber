package com.djuber.djuberbackend.Application.Services.Client.Mapper;
import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Domain.Admin.Admin;
import com.djuber.djuberbackend.Domain.Client.Client;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ClientMapper {

    public List<ClientResult> map(List<Client> clients){
        List<ClientResult> clientResults = new ArrayList<>();
        for(Client client : clients){
            clientResults.add(new ClientResult(client));
        }
        return clientResults;
    }

    public Page<ClientResult> map(Page<Client> clientPage){
        return clientPage.map(ClientResult::new);
    }
}
