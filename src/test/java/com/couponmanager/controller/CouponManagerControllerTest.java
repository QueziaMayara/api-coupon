package com.couponmanager.controller;

import com.couponmanager.exception.CouponDomainException;
import com.couponmanager.exception.CouponNotFoundException;
import com.couponmanager.exception.handler.GlobalExceptionHandler;
import com.couponmanager.service.CouponManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CouponManagerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CouponManagerService service;

    @InjectMocks
    private CouponManagerController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateCouponSuccessfully() throws Exception {
        when(service.create(any())).thenReturn(null);

        mockMvc.perform(post("/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "ABC123",
                                    "description": "Discount coupon",
                                    "discountValue": 10.00,
                                    "expirationDate": "2027-12-31T23:59:59",
                                    "published": true
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenCodeIsEmpty() throws Exception {
        mockMvc.perform(post("/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "",
                                    "description": "Discount coupon",
                                    "discountValue": 10.00,
                                    "expirationDate": "2027-12-31T23:59:59",
                                    "published": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDescriptionIsEmpty() throws Exception {
        mockMvc.perform(post("/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "ABC123",
                                    "description": "",
                                    "discountValue": 10.00,
                                    "expirationDate": "2027-12-31T23:59:59",
                                    "published": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDiscountIsBelowMinimum() throws Exception {
        mockMvc.perform(post("/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "ABC123",
                                    "description": "Discount coupon",
                                    "discountValue": 0.49,
                                    "expirationDate": "2027-12-31T23:59:59",
                                    "published": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenExpirationDateIsInThePast() throws Exception {
        mockMvc.perform(post("/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "ABC123",
                                    "description": "Discount coupon",
                                    "discountValue": 10.00,
                                    "expirationDate": "2020-01-01T00:00:00",
                                    "published": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenCodeIsInvalid() throws Exception {
        when(service.create(any()))
                .thenThrow(new CouponDomainException(
                        "Code must contain exactly 6 characters"));

        mockMvc.perform(post("/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "ABCDE",
                                    "description": "Discount coupon",
                                    "discountValue": 10.00,
                                    "expirationDate": "2027-12-31T23:59:59",
                                    "published": true
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldDeleteCouponSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/coupons/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenCouponDoesNotExist() throws Exception {
        doThrow(new CouponNotFoundException(999L))
                .when(service)
                .delete(999L);

        mockMvc.perform(delete("/v1/coupons/999"))
                .andExpect(status().isNotFound());
    }
}
