package com.example.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long pdId;
    private String pdName;
    private double pdPrice;
    private String pdImage;
    private String pdCategory;
    private String pdDescribe;
    private String pdColor;
    private int pdRating;
}