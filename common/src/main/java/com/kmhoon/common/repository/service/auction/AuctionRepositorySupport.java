package com.kmhoon.common.repository.service.auction;

import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.entity.service.auction.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuctionRepositorySupport {

    Page<Auction> findAllByItemTypeAndTitleAndPageable(ItemType itemType, String title, Pageable pageable);
}
