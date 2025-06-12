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

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
     long id;
     String username; // ← BẮT BUỘC PHẢI CÓ
     String note;
     String paymentMethod;
     double totalPrice;
     int totalQuantity;
     LocalDate orderDate;
     double shippingFee;
     Set<OrderDetail> details;
     String status;

     public long getId() {
          return id;
     }

     public void setId(long id) {
          this.id = id;
     }

     public String getStatus() {
          return status;
     }

     public void setStatus(String status) {
          this.status = status;
     }

     public String getNote() {
          return note;
     }

     public void setNote(String note) {
          this.note = note;
     }

     public String getPaymentMethod() {
          return paymentMethod;
     }

     public void setPaymentMethod(String paymentMethod) {
          this.paymentMethod = paymentMethod;
     }

     public int getTotalQuantity() {
          return totalQuantity;
     }

     public void setTotalQuantity(int totalQuantity) {
          this.totalQuantity = totalQuantity;
     }

     public LocalDate getOrderDate() {
          return orderDate;
     }

     public void setOrderDate(LocalDate orderDate) {
          this.orderDate = orderDate;
     }

     public double getTotalPrice() {
          return totalPrice;
     }

     public void setTotalPrice(double totalPrice) {
          this.totalPrice = totalPrice;
     }

     public double getShippingFee() {
          return shippingFee;
     }

     public void setShippingFee(double shippingFee) {
          this.shippingFee = shippingFee;
     }

     public Set<OrderDetail> getDetails() {
          return details;
     }

     public void setDetails(Set<OrderDetail> details) {
          this.details = details;
     }

     public String getUsername() {
          return username;
     }

     public void setUsername(String username) {
          this.username = username;
     }
}


