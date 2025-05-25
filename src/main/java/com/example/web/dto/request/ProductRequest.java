package com.example.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String pdName;
    private double pdPrice;
    private String pdImage;
    private String pdCategory;
    private String pdDescribe;
    private String pdColor;
    private int pdRating;
}