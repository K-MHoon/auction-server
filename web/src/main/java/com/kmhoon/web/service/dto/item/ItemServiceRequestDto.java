package com.kmhoon.web.service.dto.item;

import com.kmhoon.common.enums.ItemType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ItemServiceRequestDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static final class Add {

        private String name;
        private ItemType type;
        private String description;
        private List<MultipartFile> images = new ArrayList<>();
        private List<MultipartFile> documents = new ArrayList<>();
    }
}
