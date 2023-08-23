package pl.xavras.FoodOrder.util.integration.support;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
                .given()
                .pathParam("restaurantName", restaurantName)
                .when()
                .get("/api/restaurant/{restaurantName}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(RestaurantDTO.class);
    }

    default RestaurantDTO updateRestaurantByName(final String restaurantName,
                                                 final String restaurantNewName,
                                                 final String restaurantNewPhone,
                                                 final String restaurantNewEmail
                                                 ) {
        return requestSpecification()
                .given()
                .pathParam("restaurantName", restaurantName)
                .queryParam("newName", restaurantNewName)
                .queryParam("newPhone", restaurantNewPhone)
                .queryParam("newEmail", restaurantNewEmail)
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/restaurant/edit/{restaurantName}")
                .then()
                .statusCode(200)
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
