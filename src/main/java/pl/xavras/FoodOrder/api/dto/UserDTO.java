package pl.xavras.FoodOrder.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String role;
}
