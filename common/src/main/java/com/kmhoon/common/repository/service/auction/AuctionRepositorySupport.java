package com.kmhoon.common.repository.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.enums.AuctionType;
import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.auction.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionRepositorySupport {

    Page<Auction> findAllByItemTypeAndItemNameAndPageable(ItemType itemType, String itemName, Pageable pageable);

    Page<Auction> findAllByMyAuctionList(AuctionStatus auctionStatus, AuctionType auctionType, ItemType itemType, String itemName, User user, Pageable pa
    );
}
