package com.kmhoon.web.controller.dto.item.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateItemControllerRequest {

    @NotBlank(message = "{user.email.not-blank}")
    private String email;

    @NotBlank(message = "{user.password.not-blank}")
    private String password;

    @NotBlank(message = "{user.name.not-blank}")
    private String name;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();
}
