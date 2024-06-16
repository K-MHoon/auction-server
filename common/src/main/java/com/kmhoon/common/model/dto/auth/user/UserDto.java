package com.kmhoon.common.model.dto.auth.user;

import com.kmhoon.common.model.entity.auth.user.User;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    public static final class Simple {

        private Long sequence;
        private String email;
        private String name;

        public static Simple of(User user) {
            if(Objects.isNull(user)) return new Simple();
            return new Simple(user.getSequence(), user.getEmail(), user.getName());
        }
    }
}
