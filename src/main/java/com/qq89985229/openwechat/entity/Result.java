package com.qq89985229.openwechat.entity;
import lombok.Builder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 2279166027103619577L;
    private Integer code;
    private T data;
    private String message;
    private LocalDateTime currentTime;

    public static <T> Result success(T data) {
        return Result.builder()
                .code(0)
                .data(data)
                .message("操作成功")
                .currentTime(LocalDateTime.now())
                .build();
    }
    public static Result error(String message) {
        return Result.builder()
                .code(-1)
                .message(message)
                .currentTime(LocalDateTime.now())
                .build();
    }

}