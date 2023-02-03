package com.djuber.djuberbackend.BackendTesting.RepositoryTests;

import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Client.ClientSigningType;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ClientRepositoryTest {

    @Autowired
    private IClientRepository clientRepository;

    @Test
    public void shouldSaveClients(){
        Client client = new Client(1L, null, "Client" , "LClient" , "Novi Sad", "123345", true, 5512D, ClientSigningType.DEFAULT,new HashSet<>(),new HashSet<>(),new HashSet<>(), new HashSet<>(), false,false,"",new Date(),"",false);
        Client client2 = new Client(2L, null, "Client" , "LClient" , "Novi Sad", "123345", true, 5512D, ClientSigningType.DEFAULT,new HashSet<>(),new HashSet<>(),new HashSet<>(), new HashSet<>(), false,false,"",new Date(),"",false);
        List<Client> clients = new ArrayList<>();
        clients.add(client);
        clients.add(client2);

        List<Client> savedClients = clientRepository.saveAll(clients);

        assertThat(savedClients.size()).isEqualTo(clients.size());
        assertThat(savedClients.get(0)).usingRecursiveComparison().ignoringFields("id").isEqualTo(clients.get(0));
        assertThat(savedClients.get(1)).usingRecursiveComparison().ignoringFields("id").isEqualTo(clients.get(1));
    }

}
