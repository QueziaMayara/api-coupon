package com.couponmanager.repository;

import com.couponmanager.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponManagerRepository extends JpaRepository<CouponEntity, Long> {

    Optional<CouponEntity> findByIdAndDeletedFalse(Long id);
}