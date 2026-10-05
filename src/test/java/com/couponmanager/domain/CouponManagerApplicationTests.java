package com.couponmanager.domain;

import com.couponmanager.exception.CouponDomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

	private static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(1);

	@Test
	void shouldCreateCouponWithValidData() {
		Coupon coupon = new Coupon(
				"ABC123",
				"Discount coupon",
				BigDecimal.TEN,
				FUTURE_DATE,
				true
		);

		assertEquals("ABC123", coupon.getCode());
		assertEquals("Discount coupon", coupon.getDescription());
		assertEquals(BigDecimal.TEN, coupon.getDiscountValue());
		assertEquals(FUTURE_DATE, coupon.getExpirationDate());
		assertTrue(coupon.isPublished());
	}

	@Test
	void shouldCreateUnpublishedCoupon() {
		Coupon coupon = new Coupon(
				"ABC123",
				"Discount coupon",
				BigDecimal.TEN,
				FUTURE_DATE,
				false
		);

        assertFalse(coupon.isPublished());
	}

	@Test
	void shouldRemoveSpecialCharactersFromCode() {
		Coupon coupon = new Coupon(
				"A@BC#123",
				"Discount coupon",
				BigDecimal.TEN,
				FUTURE_DATE,
				true
		);

		assertEquals("ABC123", coupon.getCode());
	}

	@Test
	void shouldRejectNullCode() {
		assertThrows(
				CouponDomainException.class,
				() -> createCoupon(null)
		);
	}

	@Test
	void shouldRejectCodeWithLessThanSixCharacters() {
		assertThrows(CouponDomainException.class, () -> createCoupon("ABCDE"));
	}

	@Test
	void shouldRejectCodeWithMoreThanSixCharacters() {
		assertThrows(CouponDomainException.class, () -> createCoupon("ABCDEFG"));
	}

	@Test
	void shouldRejectCodeWhenSpecialCharactersResultInInvalidLength() {
		assertThrows(CouponDomainException.class, () -> createCoupon("AB@12"));
	}

	@Test
	void shouldRejectNullDiscount() {
		assertThrows(
				CouponDomainException.class,
				() -> new Coupon(
						"ABC123",
						"Discount coupon",
						null,
						FUTURE_DATE,
						true
				)
		);
	}

	@Test
	void shouldRejectDiscountBelowMinimum() {
		assertThrows(
				CouponDomainException.class,
				() -> new Coupon(
						"ABC123",
						"Discount coupon",
						BigDecimal.valueOf(0.49),
						FUTURE_DATE,
						true
				)
		);
	}

	@Test
	void shouldAcceptMinimumDiscount() {
		assertDoesNotThrow(() ->
				new Coupon(
						"ABC123",
						"Discount coupon",
						BigDecimal.valueOf(0.50),
						FUTURE_DATE,
						true
				)
		);
	}

	@Test
	void shouldAcceptHighDiscountWithoutMaximumLimit() {
		assertDoesNotThrow(() ->
				new Coupon(
						"ABC123",
						"Discount coupon",
						BigDecimal.valueOf(999999.99),
						FUTURE_DATE,
						true
				)
		);
	}

	@Test
	void shouldRejectNullExpirationDate() {
		assertThrows(
				CouponDomainException.class,
				() -> new Coupon(
						"ABC123",
						"Discount coupon",
						BigDecimal.TEN,
						null,
						true
				)
		);
	}

	@Test
	void shouldRejectExpiredCoupon() {
		assertThrows(
				CouponDomainException.class,
				() -> new Coupon(
						"ABC123",
						"Discount coupon",
						BigDecimal.TEN,
						LocalDate.now().minusDays(1),
						true
				)
		);
	}

	@Test
	void shouldAcceptFutureExpirationDate() {
		assertDoesNotThrow(() ->
				new Coupon(
						"ABC123",
						"Discount coupon",
						BigDecimal.TEN,
						FUTURE_DATE,
						true
				)
		);
	}

	@Test
	void shouldRejectNullDescription() {
		assertThrows(
				CouponDomainException.class,
				() -> new Coupon(
						"ABC123",
						null,
						BigDecimal.TEN,
						FUTURE_DATE,
						true
				)
		);
	}

	@Test
	void shouldRejectBlankDescription() {
		assertThrows(
				CouponDomainException.class,
				() -> new Coupon(
						"ABC123",
						"   ",
						BigDecimal.TEN,
						FUTURE_DATE,
						true
				)
		);
	}

	private void createCoupon(String code) {
		new Coupon(
				code,
				"Discount coupon",
				BigDecimal.TEN,
				FUTURE_DATE,
				true
		);
	}
}
