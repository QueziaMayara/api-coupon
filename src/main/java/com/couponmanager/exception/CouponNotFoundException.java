package com.couponmanager.exception;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(Long id) {
        super("Coupon with id " + id + " was not found");
    }
}