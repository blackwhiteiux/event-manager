package dev.sorokin.eventmanager.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<dev.sorokin.eventmanager.web.ErrorMessageResponse> handleGenericException(
            Exception e) {
        log.error("Internal server error", e);
        var error = new dev.sorokin.eventmanager.web.ErrorMessageResponse(
                "Внутренняя ошибка сервера",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }


    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<dev.sorokin.eventmanager.web.ErrorMessageResponse> handleValidationException(Exception e) {
        log.error("Got validation exception", e);

        String detailedMessage;
        if (e instanceof MethodArgumentNotValidException validationEx) {
            detailedMessage = validationEx.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else {
            detailedMessage = e.getMessage();
        }

        var error = new dev.sorokin.eventmanager.web.ErrorMessageResponse(
                "Ошибка валидации",
                detailedMessage,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<dev.sorokin.eventmanager.web.ErrorMessageResponse> handleNotFoundException(
            EntityNotFoundException e) {
        log.error("Got not found exception", e);
        var error = new dev.sorokin.eventmanager.web.ErrorMessageResponse(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
