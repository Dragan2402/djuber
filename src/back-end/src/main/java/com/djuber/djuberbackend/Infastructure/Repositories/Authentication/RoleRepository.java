package com.djuber.djuberbackend.Infastructure.Repositories.Authentication;

import com.djuber.djuberbackend.Domain.Authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
