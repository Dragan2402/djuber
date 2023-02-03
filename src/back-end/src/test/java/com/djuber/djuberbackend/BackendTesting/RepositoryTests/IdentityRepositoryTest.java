package com.djuber.djuberbackend.BackendTesting.RepositoryTests;

import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class IdentityRepositoryTest {
    @Autowired
    private IIdentityRepository identityRepository;

    @Test
    public void shouldFindByEmail(){

        Identity identity = identityRepository.findByEmail("pero@maildrop.cc");

        assertThat(identity).isNotNull();
    }

    @Test
    public void shouldReturnNullWhenEmailNotExists(){

        Identity identity = identityRepository.findByEmail("pe23323ro@maildrop.cc");

        assertThat(identity).isNull();
    }
}