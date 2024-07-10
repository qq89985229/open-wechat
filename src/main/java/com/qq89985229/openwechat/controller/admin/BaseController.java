package com.qq89985229.openwechat.controller.admin;
import com.qq89985229.openwechat.entity.QAuthorizer;
import com.qq89985229.openwechat.service.AuthorizerService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.open.api.*;
import reactor.core.publisher.Mono;

public class BaseController {
    @Resource
    WxOpenService wxOpenService;
    @Resource
    AuthorizerService authorizerService;

    // 获取公共服务类
    protected WxOpenComponentService getWxOpenComponentService(){
        return wxOpenService.getWxOpenComponentService();
    }

    protected WxMpMaterialService getWxMpMaterialService(String appId){
        return wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getMaterialService();
    }

    // 获取认证服务类
    protected WxOpenMaAuthService getWxOpenMaAuthService(String appId){
        return wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId).getAuthService();
    }

    protected WxOpenMaBasicService getWxOpenMaBasicService(String appId){
        return wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId).getBasicService();
    }

    protected WxOpenMaService getWxOpenMaService(String appId){
        return wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId);
    }

    protected WxOpenMaPrivacyService getWxOpenMaPrivacyService(String appId){
        return wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId).getPrivacyService();
    }

    /**
     * 更新授权信息
     */
    protected Mono<Void> updateAuthorizerInfo(String appId){
        return this.authorizerService.findOne(QAuthorizer.authorizer.appId.eq(appId))
                .flatMap(authorizer -> Mono.fromCallable(() -> {
                    var authorizerInfo = wxOpenService.getWxOpenComponentService().getAuthorizerInfo(authorizer.getAppId());
                    authorizer.setUserName(authorizerInfo.getAuthorizerInfo().getUserName());
                    authorizer.setAuthorizerInfo(authorizerInfo);
                    return authorizer;
                }))
                .onErrorResume(WxErrorException.class, Mono::error)
                .flatMap(authorizerService::save)
                .then();
    }
}
