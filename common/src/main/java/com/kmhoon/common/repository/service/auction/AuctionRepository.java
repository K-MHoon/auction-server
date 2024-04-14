package com.kmhoon.common.repository.service.auction;

import com.kmhoon.common.model.entity.service.auction.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    Page<Auction> findAllByStatusOrderByCreatedAt(String statusCode, Pageable pageable);
}
