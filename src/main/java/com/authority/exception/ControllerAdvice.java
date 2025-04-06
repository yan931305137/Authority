package com.authority.exception;

import com.authority.otp.exception.BadRequestException;
import com.authority.otp.exception.SendOtpException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Log4j2
public class ControllerAdvice {

    private final static String ERROR_PROPERTY = "error";

    /**
     * 处理NotFoundException异常
     * @param e 异常对象
     * @return ResponseEntity对象
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_PROPERTY, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * 处理BadRequestException异常
     * @param e 异常对象
     * @return ResponseEntity对象
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_PROPERTY, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理通用RuntimeException异常
     * @param e 异常对象
     * @return ResponseEntity对象
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleCommonException(RuntimeException e) {
        log.error("内部服务器错误发生", e);
        return new ResponseEntity<>(Collections.singletonMap(ERROR_PROPERTY, "内部服务器错误发生"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
