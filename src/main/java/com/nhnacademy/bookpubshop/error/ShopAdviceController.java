package com.nhnacademy.bookpubshop.error;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역적으로 예외를 잡기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 */
@Log4j2
@RestControllerAdvice
public class ShopAdviceController {

    /**
     * 벨리데이션 오류를 잡기위한 메서드입니다.
     *
     * @param exception 벨리데이션 오류시 발생하는 에러가 받아서 들어옵니다.
     * @return response entity 에러메세지들이 400 메시지를 받고 반환.
     * @author : 유호철
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionResponse>> validationException(
            MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException exception : {}", exception.getMessage());

        List<ExceptionResponse> exceptions = exception.getBindingResult().getAllErrors().stream()
                .map(defaultMessage -> new ExceptionResponse(defaultMessage.getDefaultMessage()))
                .collect(Collectors.toList());
        log.warn("Error value exceptions : {}", exceptions);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptions);
    }
}