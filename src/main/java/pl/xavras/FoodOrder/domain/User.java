package pl.xavras.FoodOrder.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
public class User {

    String username;
    String password;
    String name;
    String surname;
    String phone;
    String email;
    String role;

}
