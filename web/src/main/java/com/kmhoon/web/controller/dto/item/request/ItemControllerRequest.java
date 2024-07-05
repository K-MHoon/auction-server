package com.kmhoon.web.controller.dto.item.request;

import com.kmhoon.common.enums.ItemType;
import com.kmhoon.web.service.dto.item.request.ItemServiceRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemControllerRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static final class Add {

        @NotBlank
        private String name;

        @NotNull
        private ItemType type;

        @NotBlank
        private String description;

        @Builder.Default
        @NotNull
        private List<MultipartFile> images = new ArrayList<>();

        @Builder.Default
        @NotNull
        private List<MultipartFile> documents = new ArrayList<>();

        public ItemServiceRequestDto.Add toServiceRequest() {
            return ItemServiceRequestDto.Add.builder()
                    .name(this.name)
                    .type(this.type)
                    .description(this.description)
                    .images(this.images)
                    .documents(this.documents)
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @ToString
    public final static class Delete {

        @UniqueElements
        @NotNull
        @Builder.Default
        private List<Long> seqList = new ArrayList<>();
    }
}
