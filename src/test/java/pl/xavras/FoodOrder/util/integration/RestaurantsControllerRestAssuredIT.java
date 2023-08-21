package pl.xavras.FoodOrder.util.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantsDTO;
import pl.xavras.FoodOrder.util.DtoFixtures;
import pl.xavras.FoodOrder.util.integration.configuration.RestAssuredIntegrationTestBase;
import pl.xavras.FoodOrder.util.integration.support.RestaurantsControllerTestSupport;

import java.util.regex.Pattern;


public class RestaurantsControllerRestAssuredIT
        extends RestAssuredIntegrationTestBase
        implements RestaurantsControllerTestSupport
{

    @Test
    public void thatRestaurantsListCanBeRetrievedCorrectly() {
        //given
        RestaurantDTO restaurant1 = DtoFixtures.someRestaurantDTO1();
        RestaurantDTO restaurant2 = DtoFixtures.someRestaurantDTO2();

        //when
        saveRestaurant(restaurant1);
        saveRestaurant(restaurant2);

        RestaurantsDTO restaurantsDTO = listRestaurants();

        //then
        Assertions.assertThat(restaurantsDTO.getRestaurants())
                .contains(restaurant1, restaurant2);

    }
//
//
    @Test
    public void thatRestaurantCanBeCreatedCorrectly() {
        //given
        RestaurantDTO restaurant1 = DtoFixtures.someRestaurantDTO3();

        //when
        ExtractableResponse<Response> response = saveRestaurant(restaurant1);

        //then
        String responseAsString = response.body().asString();
        Assertions.assertThat(responseAsString).isEmpty();
        Assertions.assertThat(response.headers().get("Location").getValue())
                .matches(Pattern.compile("/restaurant/"+restaurant1.getName()));

    }

}
