package com.kmhoon.web.service.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
@EqualsAndHashCode
public class PageResponseDto<E> {

    private List<E> dtoList;
    private List<Integer> pageNumList;
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    private PageResponseDto(List<E> dtoList, Pageable pageable, long totalCount) {
        this.dtoList = dtoList;
        this.totalCount = (int)totalCount;

        int end = (int)(Math.ceil((pageable.getPageNumber()+ 1)  / 10.0 )) * 10;
        int start = end - 9;
        int last = (int)(Math.ceil((totalCount/(double)pageable.getPageSize())));
        end = Math.min(end, last);
        this.prev = start > 1;
        this.next = totalCount > end * pageable.getPageSize();
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        if(prev) {
            this.prevPage = start - 1;
        }
        if(next) {
            this.nextPage = end + 1;
        }
        this.totalPage = this.pageNumList.size();
        this.current = pageable.getPageNumber();
    }
}