package com.example.web.dto.request;

import com.example.web.entity.OrderDetail;
import com.example.web.entity.PaymentMethod;
import com.example.web.entity.Product;
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
public class OrderRequest {
    User user;
    private String note;
    private PaymentMethod paymentMethod;
    private double totalPrice;
    private int totalQuantity;
    private double shippingFee;
    private Set<OrderDetail> details;
    private Date orderDate;
}
