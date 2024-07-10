package com.qq89985229.openwechat.dto;
import com.alibaba.fastjson.JSONObject;
import com.qq89985229.openwechat.entity.Privacy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.chanjar.weixin.open.bean.ma.WxFastMaCategory;
import java.util.List;

public class WechatOpenDto {
    @Data
    public static class AppId{
        private String appId;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ModifyHeadImageParam extends AppId{
        private String mediaId;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class ModifySignatureParam extends AppId{
        private String signature;
    }

    @Data
    public static class LoginParam {
        private String username;
        private String password;
    }

    @Data
    public static class FastRegister {
        private String name;
        private String code;
        private String codeType;
        private String legalPersonaWechat;
        private String legalPersonaName;
    }

    @Data
    public static class AddToTemplate{
        private Long draftId;
    }
    @Data
    public static class DeleteTemplate{
        private Long templateId;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class NicknameParam extends AppId{
        private String nickname;
        private String idCard;
        private String license;
        private String namingOtherStuff1;
        private String namingOtherStuff2;
    }

    @Data
    public static class Category{
        private String value;
        private String label;
        private List<Category> children;
        private Boolean isLeaf;
    }
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class AddCategory extends AppId{
        private WxFastMaCategory category;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class DeleteCategory extends AppId{
        private Integer first;
        private Integer second;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class ModifyDomain extends AppId{
        private List<String> requestDomain;
        private List<String> wsRequestDomain;
        private List<String> uploadDomain;
        private List<String> downloadDomain;
    }
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class SetWebViewDomain extends AppId {
        private List<String> webviewdomainList;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class CodeCommit extends AppId{
        private Long templateId;
        private String userVersion;
        private String userDesc;
        private JSONObject ext;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class BindTester extends AppId{
        private String wechatId;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class UnbindTester extends AppId{
        private String userStr;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class PrivacySetting extends AppId{
        private List<Privacy> privacyList;
    }


}
