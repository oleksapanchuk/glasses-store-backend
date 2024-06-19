package com.oleksa.ecommerce.repository;

import com.oleksa.ecommerce.entity.Order;
import com.oleksa.ecommerce.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void whenFindByOrderTrackingNumber_thenReturnOrder() {
        // given
        Order order = new Order();
        order.setOrderTrackingNumber("123456");
        entityManager.persist(order);
        entityManager.flush();

        // when
        Optional<Order> found = orderRepository.findByOrderTrackingNumber(order.getOrderTrackingNumber());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getOrderTrackingNumber()).isEqualTo(order.getOrderTrackingNumber());
    }

    @Test
    public void whenFindByUserUsernameOrderByDateCreatedDesc_thenReturnOrders() {
        // given
        User user = User.builder()
                .email("test@email.com")
                .password("password")
                .build();
        entityManager.persist(user);

        Order order1 = new Order();
        order1.setUser(user);
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setUser(user);
        entityManager.persist(order2);

        entityManager.flush();

        // when
        Page<Order> found = orderRepository.findByUserEmailOrderByDateCreatedDesc(user.getUsername(), PageRequest.of(0, 5));

        // then
        assertThat(found.getContent()).hasSize(2);
        assertThat(found.getContent()).contains(order1, order2);
    }
}
