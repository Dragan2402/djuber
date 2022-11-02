package com.djuber.djuberbackend.Infastructure.Repositories.Client;

import com.djuber.djuberbackend.Domain.Client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientRepository extends JpaRepository<Client, Long> {
}
