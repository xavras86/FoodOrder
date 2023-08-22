package pl.xavras.FoodOrder.util.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.xavras.FoodOrder.util.integration.configuration.AbstractIntegrationTest;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginControllerIT extends AbstractIntegrationTest {

    private final TestRestTemplate testRestTemplate;


    @Test
    void thatMainPagesRequiredSigningIn() {
        String customer = String.format("http://localhost:%s%s/customer", port, basePath);
        String customerAddress = String.format("http://localhost:%s%s/customer/address", port, basePath);
        String customerSubmitAddress = String.format("http://localhost:%s%s/customer/submit-address", port, basePath);
        String customerOrdersForm = String.format("http://localhost:%s%s/customer/orders-form", port, basePath);
        String customerOrders = String.format("http://localhost:%s%s/customer/orders", port, basePath);
        String customerRestaurants = String.format("http://localhost:%s%s/customer/restaurants", port, basePath);
        String customerStreets = String.format("http://localhost:%s%s/customer/streets", port, basePath);
        String owner = String.format("http://localhost:%s%s/owner", port, basePath);
        String ownerOrders = String.format("http://localhost:%s%s/owner/orders", port, basePath);
        String ownerRestaurants = String.format("http://localhost:%s%s/owner/restaurants", port, basePath);

        String customerPage = this.testRestTemplate.getForObject(customer, String.class);
        String customerAddressPage = this.testRestTemplate.getForObject(customerAddress, String.class);
        String customerSubmitAddressPage = this.testRestTemplate.getForObject(customerSubmitAddress, String.class);
        String customerOrdersFormPage = this.testRestTemplate.getForObject(customerOrdersForm, String.class);
        String customerOrdersPage = this.testRestTemplate.getForObject(customerOrders, String.class);
        String customerRestaurantsPage = this.testRestTemplate.getForObject(customerRestaurants, String.class);
        String customerStreetsPage = this.testRestTemplate.getForObject(customerStreets, String.class);
        String ownerPage = this.testRestTemplate.getForObject(owner, String.class);
        String ownerOrdersPage = this.testRestTemplate.getForObject(ownerOrders, String.class);
        String ownerRestaurantsPage = this.testRestTemplate.getForObject(ownerRestaurants, String.class);


        Assertions.assertThat(customerPage).contains("Login to FoodOrder");
        Assertions.assertThat(customerAddressPage).contains("Login to FoodOrder");
        Assertions.assertThat(customerSubmitAddressPage).contains("Login to FoodOrder");
        Assertions.assertThat(customerOrdersFormPage).contains("Login to FoodOrder");
        Assertions.assertThat(customerOrdersPage).contains("Login to FoodOrder");
        Assertions.assertThat(customerRestaurantsPage).contains("Login to FoodOrder");
        Assertions.assertThat(customerStreetsPage).contains("Login to FoodOrder");
        Assertions.assertThat(ownerPage).contains("Login to FoodOrder");
        Assertions.assertThat(ownerOrdersPage).contains("Login to FoodOrder");
        Assertions.assertThat(ownerRestaurantsPage).contains("Login to FoodOrder");
    }
}
