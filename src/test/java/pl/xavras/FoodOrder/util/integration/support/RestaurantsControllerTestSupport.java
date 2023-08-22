package pl.xavras.FoodOrder.util.integration.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import pl.xavras.FoodOrder.api.controller.rest.RestaurantRestController;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantsDTO;


public interface RestaurantsControllerTestSupport {
    RequestSpecification requestSpecification();


    default RestaurantsDTO listRestaurants() {
        return requestSpecification()
                .get(RestaurantRestController.RESTAURANT)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(RestaurantsDTO.class);
    }

    default RestaurantDTO getRestaurant(final String path) {
        return requestSpecification()
                .get(path)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(RestaurantDTO.class);
    }


    default RestaurantDTO getRestaurantByName(final String restaurantName) {
        return requestSpecification()
                .get("/api/restaurant/", restaurantName)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(RestaurantDTO.class);
    }

    default ExtractableResponse<Response> saveRestaurant(final RestaurantDTO restaurantDTO) {
        return requestSpecification()
                .body(restaurantDTO)
                .post(RestaurantRestController.RESTAURANT_ADD)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .and()
                .extract();
    }


}
