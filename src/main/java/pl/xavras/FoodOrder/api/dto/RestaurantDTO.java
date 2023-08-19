package pl.xavras.FoodOrder.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {


    private String name;
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phone;
    @Email
    private String email;

    private String country;
    private String city;
    private String street;
    private String buildingNumber;

    public Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        Optional.ofNullable(name).ifPresent(value -> result.put("name", value));
        Optional.ofNullable(phone).ifPresent(value -> result.put("phone", value));
        Optional.ofNullable(email).ifPresent(value -> result.put("email", value));
        Optional.ofNullable(country).ifPresent(value -> result.put("country", value));
        Optional.ofNullable(city).ifPresent(value -> result.put("city", value));
        Optional.ofNullable(street).ifPresent(value -> result.put("street", value));
        Optional.ofNullable(buildingNumber).ifPresent(value -> result.put("buildingNumber", value));
        return result;
    }

}
