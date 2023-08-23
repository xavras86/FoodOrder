package pl.xavras.FoodOrder.util.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantsDTO;
import pl.xavras.FoodOrder.util.DtoFixtures;
import pl.xavras.FoodOrder.util.integration.configuration.RestAssuredIntegrationTestBase;
import pl.xavras.FoodOrder.util.integration.support.RestaurantsControllerTestSupport;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RestaurantsControllerRestAssuredIT
        extends RestAssuredIntegrationTestBase
        implements RestaurantsControllerTestSupport {


    @Test
    void thatRestaurantCanBeCreatedCorrectly() {
        //given
        RestaurantDTO restaurant1 = DtoFixtures.someRestaurantDTO3();

        //when
        ExtractableResponse<Response> response = saveRestaurant(restaurant1);

        //then
        String responseAsString = response.body().asString();
        Assertions.assertThat(responseAsString).isEmpty();
        Assertions.assertThat(response.headers().get("Location").getValue())
                .matches(Pattern.compile("/api/restaurant/add/" + restaurant1.getName()));

    }

    @Test
    void thatCreatedRestaurantCanBeRetrievedCorrectly() {

        //given
        RestaurantDTO restaurant = DtoFixtures.someRestaurantDTO1();

        //when
        ExtractableResponse<Response> response = saveRestaurant(restaurant);
        String restaurantDetailsPath = response.headers().get("Location").getValue();
        log.info("restaurantDetailsPath : " + restaurantDetailsPath);

        RestaurantDTO restaurantByPath = getRestaurant("/api/restaurant/" + restaurant.getName());
        RestaurantDTO restaurantByName = getRestaurantByName(restaurant.getName());

        //then
        Assertions.assertThat(restaurantByPath)
                .usingRecursiveComparison()
                .isEqualTo(restaurant);
        Assertions.assertThat(restaurantByName)
                .usingRecursiveComparison()
                .isEqualTo(restaurant);

    }

    @Test
    void thatUpdatedRestaurantCanBeRetrievedCorrectly() {
        //given
        String restaurantName = "old_restaurantName";
        String newName = "new_restaurantName";
        String newPhone = "+48 333 222 111";
        String newEmail = "newRestaurantMail@gmail.com";
        RestaurantDTO restaurant1 = DtoFixtures.someRestaurantDTO1()
                .withName(restaurantName)
                .withPhone("+48 111 222 333")
                .withEmail("oldRestaurantMail@gmail.com");

        //when
        saveRestaurant(restaurant1);
        RestaurantsDTO restaurantsDTOBefore = listRestaurants();
        RestaurantDTO restaurantDTOUpdated = updateRestaurantByName(restaurantName, newName, newPhone, newEmail);
        RestaurantsDTO restaurantsDTOAfter = listRestaurants();

        //then
        assertThat(restaurantsDTOBefore.getRestaurants()).size().isEqualTo(restaurantsDTOAfter.getRestaurants().size());
        Assertions.assertThat(restaurantDTOUpdated.getName()).isEqualTo(newName);
        Assertions.assertThat(restaurantDTOUpdated.getEmail()).isEqualTo(newEmail);
        Assertions.assertThat(restaurantDTOUpdated.getPhone()).isEqualTo(newPhone);

    }


    @Test
    void thatRestaurantsListCanBeRetrievedCorrectly() {
        //given
        RestaurantDTO restaurant1 = DtoFixtures.someRestaurantDTO1().withName("another_restaurant1");
        RestaurantDTO restaurant2 = DtoFixtures.someRestaurantDTO2().withName("another_restaurant2");
        RestaurantsDTO restaurantsDTOBefore = listRestaurants();
        //when
        saveRestaurant(restaurant1);
        saveRestaurant(restaurant2);
        RestaurantsDTO restaurantsDTOAfter = listRestaurants();

        //then
        assertThat(restaurantsDTOBefore.getRestaurants()).size().isEqualTo(restaurantsDTOAfter.getRestaurants().size() - 2);
        Assertions.assertThat(restaurantsDTOAfter.getRestaurants())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(restaurant1, restaurant2);

    }


}