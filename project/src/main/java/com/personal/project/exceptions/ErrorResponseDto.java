package com.personal.project.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {

    private LocalDateTime timestamp;

    private Integer status;

    private String error;

    private String message;
}