package com.kmhoon.web.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(exclude = "password")
public class CreateUserControllerRequest {

    @NotBlank(message = "{user.email.not-blank}")
    private String email;

    @NotBlank(message = "{user.password.not-blank}")
    private String password;

    @NotBlank(message = "{user.name.not-blank}")
    private String name;
}
