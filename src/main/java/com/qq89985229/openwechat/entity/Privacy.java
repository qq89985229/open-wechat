package com.qq89985229.openwechat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.io.Serial;
import java.io.Serializable;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Privacy implements Serializable {
    @Serial
    private static final long serialVersionUID = -4452844976591601147L;
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;
    private String privacyKey;
    private String privacyDesc;
    @Transient
    private Boolean checked;
}
