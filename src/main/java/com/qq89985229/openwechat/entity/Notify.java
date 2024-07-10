package com.qq89985229.openwechat.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Notify implements Serializable {
    @Serial
    private static final long serialVersionUID = 4787715702494436500L;
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;
    private String appId;
    private String infoType;
    private String message;
    private LocalDateTime createdAt;
}
