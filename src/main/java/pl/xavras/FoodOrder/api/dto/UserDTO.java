package pl.xavras.FoodOrder.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String password;
    private String name;
    private String surname;
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phone;
    @Email
    private String email;
    private String role;

    public Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        Optional.ofNullable(name).ifPresent(value -> result.put("name", value));
        Optional.ofNullable(surname).ifPresent(value -> result.put("surname", value));
        Optional.ofNullable(phone).ifPresent(value -> result.put("phone", value));
        Optional.ofNullable(email).ifPresent(value -> result.put("email", value));
        Optional.ofNullable(role).ifPresent(value -> result.put("role", value));
        Optional.ofNullable(username).ifPresent(value -> result.put("username", value));
        Optional.ofNullable(password).ifPresent(value -> result.put("password", value));
        return result;
    }
}
