package com.djuber.djuberbackend.BackendTesting.RepositoryTests;

import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class DriverRepositoryTest {
    @Autowired
    private IDriverRepository driverRepository;

    @Test
    public void shouldSaveRide(){
        Driver driver = new Driver(1L, null, "Client" , "LClient" , "Novi Sad", "123345", true, new HashSet<>(), new HashSet<>(),null, OffsetDateTime.now(), false, false,null, false );

        Driver driverSaved = driverRepository.save(driver);

        assertThat(driverSaved).usingRecursiveComparison().ignoringFields("id").isEqualTo(driverSaved);
    }
}
