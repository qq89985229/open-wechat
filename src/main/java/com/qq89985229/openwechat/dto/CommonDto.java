package com.qq89985229.openwechat.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

public class CommonDto {
    @Data
    public static class PageQueryParam {
        private int page = 1;
        private int size = 10;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ByIds{
        private List<String> ids;
    }

}
