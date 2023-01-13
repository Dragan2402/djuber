package com.djuber.djuberbackend.Infastructure.Repositories.Client;


import com.djuber.djuberbackend.Domain.Client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c where c.identity.id = ?1 and c.deleted=false")
    Client findByIdentityId(Long identityId);

    @Query("select c from Client c where c.verificationToken = ?1 and c.deleted=false")
    Client findByVerificationToken(String token);

    @Query("select c from Client c where lower(c.firstName) like lower(concat('%', ?1,'%') ) or lower(c.lastName) like lower(concat('%', ?1,'%') ) " +
            "or concat(c.id,'') like lower(concat('%', ?1,'%') ) " +
            "or lower(c.identity.email) like lower(concat('%', ?1,'%') ) " +
            "or lower(c.city) like lower(concat('%', ?1,'%') ) ")
    Page<Client> findAllWithFilter(String filter, Pageable pageable);
}
