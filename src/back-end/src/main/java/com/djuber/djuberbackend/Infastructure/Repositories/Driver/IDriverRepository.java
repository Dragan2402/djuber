package com.djuber.djuberbackend.Infastructure.Repositories.Driver;

import com.djuber.djuberbackend.Domain.Driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IDriverRepository extends JpaRepository<Driver, Long> {

    @Query("select d from Driver d where d.identity.id = ?1")
    Driver findByIdentityId(Long identityId);
}
