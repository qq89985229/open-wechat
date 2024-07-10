package com.qq89985229.openwechat.handler;

import com.qq89985229.openwechat.entity.Result;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(RuntimeException.class)
    public Mono<Result> handleException(RuntimeException e) {
        return Mono.just(Result.error(e.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<Result> handleWebExchangeBindException(WebExchangeBindException ex) {
        var result = ex.getBindingResult();
        if (result.hasErrors()) {
            var errors = result.getAllErrors();
            var message =  errors.stream()
                    .map(error -> (FieldError)error)
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(","));
            return Mono.just(Result.error(message));
        }
        return null;
    }

    @ExceptionHandler(WxErrorException.class)
    public Mono<Result> handleWxErrorException(WxErrorException e) {
        return Mono.just(Result.error(e.getMessage()));
    }


}
