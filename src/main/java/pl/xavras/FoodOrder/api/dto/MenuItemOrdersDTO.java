package pl.xavras.FoodOrder.api.dto;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemOrdersDTO {


   private List<MenuItemOrderDTO> orders;


}
