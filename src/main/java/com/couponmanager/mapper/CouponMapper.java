package com.couponmanager.mapper;

import com.couponmanager.controller.response.CouponResponse;
import com.couponmanager.domain.Coupon;
import com.couponmanager.entity.CouponEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    default CouponEntity toEntity(Coupon coupon) {
        if (coupon == null) {
            return null;
        }

        return new CouponEntity(
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscountValue(),
                coupon.getExpirationDate(),
                coupon.isPublished()
        );
    }

    CouponResponse toResponse(CouponEntity coupon);
}