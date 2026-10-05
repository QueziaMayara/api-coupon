package com.couponmanager.mapper;

import com.couponmanager.controller.response.CouponResponse;
import com.couponmanager.domain.Coupon;
import com.couponmanager.entity.CouponEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    CouponEntity toEntity(Coupon coupon);

    CouponResponse toResponse(CouponEntity coupon);
}