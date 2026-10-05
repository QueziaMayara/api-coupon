package com.couponmanager.domain;

import com.couponmanager.exception.CouponDomainException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Coupon {

    private static final int CODE_LENGTH = 6;
    private static final BigDecimal MIN_DISCOUNT_VALUE = BigDecimal.valueOf(0.5);

    private final String code;
    private final String description;
    private final BigDecimal discountValue;
    private final LocalDateTime expirationDate;
    private final boolean published;

    public Coupon(
            String code,
            String description,
            BigDecimal discountValue,
            LocalDateTime expirationDate,
            boolean published
    ) {
        this.code = sanitizeCode(code);
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;

        validate();
    }

    private void validate() {
        if (code == null || code.length() != CODE_LENGTH) {
            throw new CouponDomainException(
                    "Coupon code must contain exactly 6 characters"
            );
        }

        if (description == null || description.isBlank()) {
            throw new CouponDomainException("Description is required");
        }

        if (discountValue == null ||
                discountValue.compareTo(MIN_DISCOUNT_VALUE) < 0) {
            throw new CouponDomainException(
                    "Discount value must be at least 0.5"
            );
        }

        if (expirationDate == null ||
                !expirationDate.isAfter(LocalDateTime.now())) {
            throw new CouponDomainException(
                    "Expiration date must be in the future"
            );
        }
    }

    private String sanitizeCode(String code) {
        if (code == null) {
            return null;
        }

        return code.replaceAll("[^a-zA-Z0-9]", "");
    }
}