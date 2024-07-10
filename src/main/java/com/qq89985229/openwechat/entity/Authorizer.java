package com.qq89985229.openwechat.entity;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.open.bean.result.WxOpenAuthorizerInfoResult;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authorizer implements Serializable {
    @Serial
    private static final long serialVersionUID = 7646098501739780918L;
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;
    private String appId;
    private String userName;
    private WxOpenAuthorizerInfoResult authorizerInfo;
    private Long authTime;
    private JSONObject ext;
    private LocalDateTime createdAt;
}
