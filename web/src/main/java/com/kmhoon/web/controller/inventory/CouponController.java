package com.kmhoon.web.controller.inventory;

import com.kmhoon.web.controller.dto.inventory.request.CouponControllerRequest;
import com.kmhoon.web.service.inventory.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CouponController {

    private final CouponService couponService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/service/coupon")
    public void buy(@RequestBody @Valid CouponControllerRequest.Buy request, Principal principal) {
        couponService.buy(request.toServiceRequest());
    }
}
