package com.couponmanager.service;

import com.couponmanager.controller.request.CouponRequest;
import com.couponmanager.controller.response.CouponResponse;
import com.couponmanager.domain.Coupon;
import com.couponmanager.entity.CouponEntity;
import com.couponmanager.exception.CouponNotFoundException;
import com.couponmanager.mapper.CouponMapper;
import com.couponmanager.repository.CouponManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponManagerService {

    private final CouponManagerRepository repository;
    private final CouponMapper mapper;

    @Transactional
    public CouponResponse create(CouponRequest request) {
        Coupon coupon = new Coupon(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );

        CouponEntity entity = mapper.toEntity(coupon);
        CouponEntity saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        CouponEntity coupon = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
        coupon.markAsDeleted(LocalDateTime.now());
    }
}