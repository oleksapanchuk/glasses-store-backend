package com.oleksa.ecommerce.repository;

import com.oleksa.ecommerce.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testUser")
                .email("test@email.com")
                .password("password")
                .build();
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        // given
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        // given
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByUsername(user.getUsername());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void whenExistsByUsername_thenReturnTrue() {
        // given
        entityManager.persist(user);
        entityManager.flush();

        // when
        boolean exists = userRepository.existsByUsername(user.getUsername());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        // given
        entityManager.persist(user);
        entityManager.flush();

        // when
        boolean exists = userRepository.existsByEmail(user.getEmail());

        // then
        assertThat(exists).isTrue();
    }
}
