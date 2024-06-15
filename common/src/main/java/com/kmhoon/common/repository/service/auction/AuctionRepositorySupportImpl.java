package com.kmhoon.common.repository.service.auction;

import com.kmhoon.common.enums.AuctionStatus;
import com.kmhoon.common.enums.ItemType;
import com.kmhoon.common.model.entity.service.auction.Auction;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.kmhoon.common.model.entity.service.auction.QAuction.auction;
import static com.kmhoon.common.model.entity.service.item.QItem.item;

public class AuctionRepositorySupportImpl extends QuerydslRepositorySupport implements AuctionRepositorySupport {
    public AuctionRepositorySupportImpl() {
        super(Auction.class);
    }

    @Override
    public Page<Auction> findAllByItemTypeAndTitleAndPageable(ItemType itemType, String title, Pageable pageable) {
        List<Auction> result = from(auction)
                .leftJoin(auction.item, item).fetchJoin()
                .where(auction.isUse.isTrue(),
                        auction.status.eq(AuctionStatus.RUNNING),
                        containsTitle(title),
                        eqItemType(itemType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(auction.startTime.desc())
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    }

    private BooleanExpression eqItemType(ItemType itemType) {
        if(Objects.isNull(itemType)) return null;
        return auction.item.type.eq(itemType);
    }

    private BooleanExpression containsTitle(String title) {
        if(!StringUtils.hasText(title)) return null;
            return auction.title.contains(title);
    }
}
