package pl.xavras.FoodOrder.util.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.util.DtoFixtures;
import pl.xavras.FoodOrder.util.integration.configuration.RestAssuredIntegrationTestBase;
import pl.xavras.FoodOrder.util.integration.support.RestaurantsControllerTestSupport;

import java.util.regex.Pattern;

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


}
