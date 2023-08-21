package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OwnerJpaRepository;
import pl.xavras.FoodOrder.util.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.xavras.FoodOrder.util.EntityFixtures.someOwnerEntity;

@DataJpaTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class OwnerRepositoryDataJpaTest {

    private OwnerJpaRepository ownerJpaRepository;


    @Test
    void testThatCreateAndFindByEmailWorksCorrectly() {

        //given
        OwnerEntity owner = someOwnerEntity();

        //when
        ownerJpaRepository.save(owner);
        Optional<OwnerEntity> foundOwner = ownerJpaRepository.findByEmail(owner.getEmail());

        //then
        assertThat(foundOwner).isPresent();
        assertThat(foundOwner.get().getEmail()).isEqualTo(owner.getEmail());
    }
}
