package com.couponmanager.controller.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse(Long id,
                             String code,
                             String description,
                             BigDecimal discountValue,
                             LocalDateTime expirationDate,
                             boolean published) {
}
