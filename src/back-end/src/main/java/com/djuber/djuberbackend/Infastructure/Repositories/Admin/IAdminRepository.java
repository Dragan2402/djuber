package com.djuber.djuberbackend.Infastructure.Repositories.Admin;

import com.djuber.djuberbackend.Domain.Admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Long> {

    @Query("select a from Admin a where a.identity.id = ?1")
    Admin findByIdentityId(Long identityId);
}
