
package com.couponmanager.service;

import com.couponmanager.controller.request.CouponRequest;
import com.couponmanager.controller.response.CouponResponse;
import com.couponmanager.domain.Coupon;
import com.couponmanager.entity.CouponEntity;
import com.couponmanager.exception.CouponDomainException;
import com.couponmanager.exception.CouponNotFoundException;
import com.couponmanager.mapper.CouponMapper;
import com.couponmanager.repository.CouponManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponManagerServiceTest {

    @Mock
    private CouponManagerRepository repository;

    @Mock
    private CouponMapper mapper;

    @InjectMocks
    private CouponManagerService service;

    @Test
    void shouldCreateCoupon() {
        CouponRequest request = new CouponRequest(
                "ABC123",
                "Discount coupon",
                new BigDecimal("10.00"),
                LocalDateTime.now().plusDays(10),
                true
        );

        CouponEntity entity = mock(CouponEntity.class);
        CouponEntity savedEntity = mock(CouponEntity.class);

        CouponResponse expected = new CouponResponse(
                1L,
                "ABC123",
                "Discount coupon",
                new BigDecimal("10.00"),
                request.expirationDate(),
                true
        );

        when(mapper.toEntity(any(Coupon.class)))
                .thenReturn(entity);

        when(repository.save(entity))
                .thenReturn(savedEntity);

        when(mapper.toResponse(savedEntity))
                .thenReturn(expected);

        CouponResponse response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("ABC123", response.code());
        assertEquals("Discount coupon", response.description());
        assertEquals(new BigDecimal("10.00"), response.discountValue());
        assertTrue(response.published());
    }

    @Test
    void shouldCreateUnpublishedCoupon() {
        CouponRequest request = new CouponRequest(
                "ABC123",
                "Discount coupon",
                new BigDecimal("0.5"),
                LocalDateTime.now().plusDays(10),
                false
        );

        CouponEntity entity = mock(CouponEntity.class);
        CouponEntity savedEntity = mock(CouponEntity.class);

        CouponResponse expected = new CouponResponse(
                2L,
                "ABC123",
                "Discount coupon",
                new BigDecimal("0.5"),
                request.expirationDate(),
                false
        );

        when(mapper.toEntity(any(Coupon.class)))
                .thenReturn(entity);

        when(repository.save(entity))
                .thenReturn(savedEntity);

        when(mapper.toResponse(savedEntity))
                .thenReturn(expected);

        CouponResponse response = service.create(request);

        assertNotNull(response);
        assertFalse(response.published());
        assertEquals(new BigDecimal("0.5"), response.discountValue());
    }

    @Test
    void shouldThrowExceptionWhenCreatingCouponWithInvalidCode() {
        CouponRequest request = new CouponRequest(
                "AB@C#12",
                "Discount coupon",
                new BigDecimal("10.00"),
                LocalDateTime.now().plusDays(10),
                false
        );

        assertThrows(CouponDomainException.class, () -> service.create(request));
    }

    @Test
    void shouldSoftDeleteCoupon() {
        CouponEntity coupon = mock(CouponEntity.class, CALLS_REAL_METHODS);

        when(repository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(coupon));

        service.delete(1L);

        assertTrue(coupon.isDeleted());
        assertNotNull(coupon.getDeletedAt());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentCoupon() {
        when(repository.findByIdAndDeletedFalse(999L))
                .thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> service.delete(999L));
    }

    @Test
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        when(repository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> service.delete(1L));
    }
}