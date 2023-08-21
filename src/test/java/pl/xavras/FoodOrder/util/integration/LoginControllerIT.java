package pl.xavras.FoodOrder.util.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.xavras.FoodOrder.util.integration.configuration.AbstractIT;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginControllerIT extends AbstractIT {

    private final TestRestTemplate testRestTemplate;


    @Test
    void thatMainPagesRequiredSigningIn() {
        String customer = String.format("http://localhost:%s%s/customer", port, basePath);
        String owner = String.format("http://localhost:%s%s/owner", port, basePath);

        String customerPage = this.testRestTemplate.getForObject(customer, String.class);
        String ownerPage = this.testRestTemplate.getForObject(owner, String.class);
        Assertions.assertThat(customerPage).contains("Login to FoodOrder");
        Assertions.assertThat(ownerPage).contains("Login to FoodOrder");
    }
}
