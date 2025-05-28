package com.example.web.dto.response;

import com.example.web.entity.OrderDetail;
import com.example.web.entity.PaymentMethod;
import com.example.web.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class OrderResponse {
     User user;
     String note;
     PaymentMethod paymentMethod;
     double totalPrice;
     int totalQuantity;
     Date orderDate;
     double shippingFee;
     Set<OrderDetail> details;
}
