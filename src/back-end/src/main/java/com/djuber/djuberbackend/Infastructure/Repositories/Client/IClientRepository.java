package com.djuber.djuberbackend.Infastructure.Repositories.Client;


import com.djuber.djuberbackend.Domain.Client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c where c.identity.id = ?1")
    Client findByIdentityId(Long identityId);
}
