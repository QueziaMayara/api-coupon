package com.couponmanager.controller.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CouponRequest(

        @NotBlank
        String code,

        @NotBlank
        String description,

        @NotNull
        @DecimalMin(value = "0.5")
        BigDecimal discountValue,

        @NotNull
        @Future
        LocalDate expirationDate,

        boolean published
) {
}