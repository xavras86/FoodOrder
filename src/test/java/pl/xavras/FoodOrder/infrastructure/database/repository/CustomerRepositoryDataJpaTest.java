package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.xavras.FoodOrder.infrastructure.database.entity.CustomerEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.CustomerJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.util.EntityFixtures;
import pl.xavras.FoodOrder.util.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.xavras.FoodOrder.util.EntityFixtures.*;

@DataJpaTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class CustomerRepositoryDataJpaTest {

    private CustomerJpaRepository customerJpaRepository;


    @Test
    void testThatCreateAndFindByEmailWorksCorrectly() {

        //given
        CustomerEntity customer = someCustomerEntity();

        //when
        customerJpaRepository.save(customer);
        Optional<CustomerEntity> foundCustomer = customerJpaRepository.findByEmail(customer.getEmail());

        //then
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getEmail()).isEqualTo(customer.getEmail());
    }
}
