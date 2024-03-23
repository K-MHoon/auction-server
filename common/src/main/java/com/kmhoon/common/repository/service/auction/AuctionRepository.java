package com.kmhoon.common.repository.service.auction;

import com.kmhoon.common.model.entity.service.auction.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
