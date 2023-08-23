package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OrderJpaRepository;
import pl.xavras.FoodOrder.util.EntityFixtures;
import pl.xavras.FoodOrder.util.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class OrderRepositoryDataJpaTest {

    private OrderJpaRepository orderJpaRepository;

    @Transactional
    @Test
    void testThatCreateAndFindByNameWorksCorrectly() {

        //given
        OrderEntity order = EntityFixtures.someOrderEntity().withOrderNumber("123");

        //when
        orderJpaRepository.save(order);
        Optional<OrderEntity> foundOrder = orderJpaRepository.findByOrderNumber("123");

        //then
        assertTrue(foundOrder.isPresent());
        assertThat(foundOrder.get().getOrderId()).isEqualTo(order.getOrderId());
        assertThat(foundOrder.get().getOrderNumber()).isEqualTo(order.getOrderNumber());
        assertThat(foundOrder.get().getCompleted()).isEqualTo(order.getCompleted());
        assertThat(foundOrder.get().getCancelled()).isEqualTo(order.getCancelled());
        assertThat(foundOrder.get().getReceivedDateTime()).isEqualTo(order.getReceivedDateTime());
        assertThat(foundOrder.get().getCompletedDateTime()).isEqualTo(order.getCompletedDateTime());
        assertThat(foundOrder.get().getTotalValue()).isEqualTo(order.getTotalValue());
    }

    @Transactional
    @Test
    public void testThatDeleteByOrderNumber() {
        // Given
        String orderNumber = "54656";
        OrderEntity orderEntity = EntityFixtures.someOrderEntity()
                .withOrderNumber(orderNumber)
                .withAddress(EntityFixtures.someAddressEntity1())
                .withCancelled(true);


        orderJpaRepository.save(orderEntity);
        int allOrderBefore = orderJpaRepository.findAll().size();
        log.info("allOrderBefore : " + allOrderBefore );

        // When
        orderJpaRepository.deleteByOrderNumber(orderNumber);
        Optional<OrderEntity> deletedOrder = orderJpaRepository.findByOrderNumber(orderNumber);
        int allOrderAfter = orderJpaRepository.findAll().size();
        log.info("allOrderAfter : " + allOrderAfter );
        // Then
        assertTrue(deletedOrder.isEmpty());
        assertEquals(allOrderBefore, allOrderAfter + 1);
    }
}


