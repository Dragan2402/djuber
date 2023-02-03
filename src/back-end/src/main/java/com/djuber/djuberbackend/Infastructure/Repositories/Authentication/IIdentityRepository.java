package com.djuber.djuberbackend.Infastructure.Repositories.Authentication;

import com.djuber.djuberbackend.Domain.Authentication.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IIdentityRepository extends JpaRepository<Identity, Long> {
    Identity findByEmail(String email);

    @Query("select i from Identity i where i.passwordResetToken = ?1 and i.deleted=false")
    Identity findIdentityByPasswordResetToken(String token);
}
