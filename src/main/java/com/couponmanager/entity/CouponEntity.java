package com.couponmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "coupons")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean deleted;

    private LocalDateTime deletedAt;

    public CouponEntity(
            String code,
            String description,
            BigDecimal discountValue,
            LocalDate expirationDate,
            boolean published
    ) {
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
    }

    public void markAsDeleted(LocalDateTime deletedAt) {
        this.deleted = true;
        this.deletedAt = deletedAt;
    }
}