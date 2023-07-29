package pl.xavras.FoodOrder.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressOrderDTO {

   private String name;
   private String surname;
   private String phone;
   private String email;

   private Integer addressId;
   private String country;
   private String city;
   private String street;
   private String buildingNumber;

   private String orderNumber;
   private String receivedDateTime;
   private String completedDateTime;
   private Boolean isCancelled;
   private Boolean isCompleted;
   private BigDecimal totalValue;


}
