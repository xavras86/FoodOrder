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
public class OrderDTO {

    private String orderNumber;
    private String receivedDateTime;
    private String completedDateTime;
    private Boolean isCancelled;
    private Boolean isCompleted;
    private BigDecimal totalValue;
//    private CustomerEntity customer;
//    private AddressEntity address;
}
