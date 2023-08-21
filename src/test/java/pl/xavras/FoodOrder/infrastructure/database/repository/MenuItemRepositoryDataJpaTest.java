package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.xavras.FoodOrder.infrastructure.database.entity.MealCategory;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.MenuItemJpaRepository;
import pl.xavras.FoodOrder.util.DomainFixtures;
import pl.xavras.FoodOrder.util.EntityFixtures;
import pl.xavras.FoodOrder.util.integration.configuration.PersistenceContainerTestConfiguration;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class MenuItemRepositoryDataJpaTest {

    private MenuItemJpaRepository menuItemJpaRepository;


    @Test
    void testThatCreateAndFindByNameWorksCorrectly() {

        //given
        MenuItemEntity menuItem = EntityFixtures.someMenuItemEntity()
                .withMenuItemId(333)
                .withName("Some beverage")
                .withPrice(new BigDecimal("33.55"))
                .withCategory(MealCategory.BEVERAGES)
                .withDescription("Some description")
                .withAvailable(true)
                .withRestaurant(EntityFixtures.someRestaurantEntity1());


        //when
        menuItemJpaRepository.save(menuItem);
        Optional<MenuItemEntity> foundMenuItem = menuItemJpaRepository.findByName("Some beverage");

        //then
        assertThat(foundMenuItem).isPresent();
        assertThat(foundMenuItem.get().getMenuItemId()).isEqualTo(333);
        assertThat(foundMenuItem.get().getName()).isEqualTo("Some beverage");
        assertThat(foundMenuItem.get().getCategory()).isEqualTo(MealCategory.BEVERAGES);
        assertThat(foundMenuItem.get().getDescription()).isEqualTo("Some description");
        assertTrue(foundMenuItem.get().getAvailable());
    }

}
