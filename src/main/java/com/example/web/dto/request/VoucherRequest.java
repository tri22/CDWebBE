package com.example.web.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {
    @Size(min = 4 ,message ="Code must longer than 4" )
    private String code;
    private String description;
    private int quantity;
    @Min(value = 1, message = "Discount must be at least 1%")
    @Max(value = 100, message = "Discount must not exceed 100%")
    private int discount;

    @Min(value = 1, message = "Discount must be at least 1%")
    @Max(value = 100, message = "Discount must not exceed 100%")
    public int getDiscount() {
        return discount;
    }

    public void setDiscount(@Min(value = 1, message = "Discount must be at least 1%") @Max(value = 100, message = "Discount must not exceed 100%") int discount) {
        this.discount = discount;
    }
}
