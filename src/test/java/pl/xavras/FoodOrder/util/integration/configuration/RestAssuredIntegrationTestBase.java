package pl.xavras.FoodOrder.util.integration.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import pl.xavras.FoodOrder.util.integration.support.AuthenticationTestSupport;
import pl.xavras.FoodOrder.util.integration.support.ControllerTestSupport;


import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class RestAssuredIntegrationTestBase
    extends AbstractIntegrationTest
    implements ControllerTestSupport, AuthenticationTestSupport {

    @LocalServerPort
    protected int port;

    @Value("${server.servlet.context-path}")
    protected String basePath;

    protected static WireMockServer wireMockServer;

    private String jSessionIdValue;

    @Autowired
    @SuppressWarnings("unused")
    protected ObjectMapper objectMapper;

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Test
    void contextLoaded() {
        assertThat(true).isTrue();
    }

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(
            wireMockConfig()
                .port(9999)
                .extensions(new ResponseTemplateTransformer(false))
        );
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @BeforeEach
    void beforeEach() {
        jSessionIdValue = login("owner", "test")
            .and()
            .cookie("JSESSIONID")
            .header(HttpHeaders.LOCATION, "http://localhost:%s%s/home".formatted(port, basePath))
            .extract()
            .cookie("JSESSIONID");
    }

    @AfterEach
    void afterEach() {
        logout()
            .and()
            .cookie("JSESSIONID", "");
        jSessionIdValue = null;
        wireMockServer.resetAll();
    }

    public RequestSpecification requestSpecification() {
        return restAssuredBase()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie("JSESSIONID", jSessionIdValue);
    }

    public RequestSpecification requestSpecificationNoAuthentication() {
        return restAssuredBase();
    }

    private RequestSpecification restAssuredBase() {
        return RestAssured
            .given()
            .config(getConfig())
            .basePath(basePath)
            .port(port);
    }

    private RestAssuredConfig getConfig() {
        return RestAssuredConfig.config()
            .objectMapperConfig(new ObjectMapperConfig()
                .jackson2ObjectMapperFactory((type, s) -> objectMapper));
    }
}
