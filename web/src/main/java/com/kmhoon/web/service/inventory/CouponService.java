package com.kmhoon.web.service.inventory;

import com.kmhoon.common.enums.CouponStatus;
import com.kmhoon.common.enums.CouponType;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.inventory.Coupon;
import com.kmhoon.common.model.entity.service.inventory.Inventory;
import com.kmhoon.common.repository.auth.user.UserRepository;
import com.kmhoon.common.repository.service.inventory.CouponRepository;
import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.code.service.item.ItemErrorCode;
import com.kmhoon.web.service.dto.inventory.request.CouponServiceRequestDto;
import com.kmhoon.web.service.user.UserCommonService;
import com.kmhoon.web.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCommonService userCommonService;

    @Transactional
    public Long buy(CouponServiceRequestDto.Buy dto) {
        User loggedInUser = userCommonService.getLoggedInUser();

        Inventory inventory = loggedInUser.getInventory();
        try {
            inventory.minusMoney(dto.getPrice());
        } catch (IllegalArgumentException ex) {
            throw new AuctionApiException(ex, ex.getMessage());
        }

        List<Coupon> couponList = LongStream.rangeClosed(0, dto.getCount()).mapToObj(c -> Coupon.builder()
                        .status(CouponStatus.UNUSED)
                        .type(CouponType.AUCTION)
                        .inventory(inventory)
                        .isUse(Boolean.TRUE)
                        .endDate(null)
                        .build())
                .collect(Collectors.toList());

        couponRepository.saveAll(couponList);

        return inventory.getMoney();
    }
}
