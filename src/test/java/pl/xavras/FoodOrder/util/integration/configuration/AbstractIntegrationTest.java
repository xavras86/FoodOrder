package pl.xavras.FoodOrder.util.integration.configuration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import pl.xavras.FoodOrder.FoodOrderApplication;


@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.yaml")
@Import(PersistenceContainerTestConfiguration.class)
@SpringBootTest(
    classes = {FoodOrderApplication.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Value("${server.servlet.context-path}")
    protected String basePath;

    @AfterEach
    void afterEach() {

    }
}
