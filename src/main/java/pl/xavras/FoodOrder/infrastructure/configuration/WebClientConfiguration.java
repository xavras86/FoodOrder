package pl.xavras.FoodOrder.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfiguration {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;
}
