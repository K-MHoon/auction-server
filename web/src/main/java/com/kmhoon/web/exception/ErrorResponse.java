package com.kmhoon.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public record ErrorResponse(String code,
                            String message,
                            @JsonInclude(JsonInclude.Include.NON_EMPTY) List<ValidationError> errors) {

    @Getter
    @Builder
    @RequiredArgsConstructor
    public record ValidationError(String field, String message) {
        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}