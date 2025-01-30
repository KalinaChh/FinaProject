package com.fmi.p_final.http;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppResponse<T> {

    private boolean success;
    private int code;
    private String message;
    private T data;
    private List<ValidationError> validationErrors = new ArrayList<>();

    public static <T> ResponseEntity<AppResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(
                AppResponse.<T>builder()
                        .success(true)
                        .code(HttpStatus.OK.value())
                        .message(message)
                        .data(data)
                        .build()
        );
    }

    public static <T> ResponseEntity<AppResponse<T>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(AppResponse.<T>builder()
                        .success(false)
                        .code(status.value())
                        .message(message)
                        .build());
    }

    public static <T> ResponseEntity<AppResponse<T>> validationError(String message, List<ValidationError> errors) {
        return ResponseEntity.badRequest()
                .body(AppResponse.<T>builder()
                        .success(false)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(message)
                        .validationErrors(errors)
                        .build());
    }

    @Getter
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}
