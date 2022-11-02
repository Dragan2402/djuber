package com.djuber.djuberbackend.Infastructure.Repositories.Driver;

import com.djuber.djuberbackend.Domain.Driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDriverRepository extends JpaRepository<Driver, Long> {
}
