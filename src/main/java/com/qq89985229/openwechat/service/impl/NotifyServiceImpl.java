package com.qq89985229.openwechat.service.impl;
import com.qq89985229.openwechat.entity.Authorizer;
import com.qq89985229.openwechat.entity.Notify;
import com.qq89985229.openwechat.entity.QAuthorizer;
import com.qq89985229.openwechat.repository.NotifyRepository;
import com.qq89985229.openwechat.service.AuthorizerService;
import com.qq89985229.openwechat.service.NotifyService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class NotifyServiceImpl extends MongoServiceImpl<Notify, NotifyRepository> implements NotifyService {
    @Resource
    AuthorizerService authorizerService;
    @Resource
    WxOpenService wxOpenService;

    @Override
    public Mono<Void> saveByWxOpenXmlMessage(WxOpenXmlMessage wxOpenXmlMessage) {
        if(wxOpenXmlMessage.getInfoType().equals("authorized"))
            return save(Notify.builder()
                    .appId(wxOpenXmlMessage.getAuthorizerAppid())
                    .infoType("authorized")
                    .message("授权成功")
                    .createdAt(LocalDateTime.now())
                    .build())
                    .flatMap(notify -> Mono.fromCallable(() ->
                                    wxOpenService.getWxOpenComponentService().getAuthorizerInfo(wxOpenXmlMessage.getAuthorizerAppid()))
                            .onErrorResume(WxErrorException.class, Mono::error)
                    )
                    .map(wxOpenAuthorizerInfoResult -> Authorizer.builder()
                            .appId(wxOpenXmlMessage.getAuthorizerAppid())
                            .authorizerInfo(wxOpenAuthorizerInfoResult)
                            .authTime(wxOpenXmlMessage.getCreateTime())
                            .build())
                    .flatMap(authorizerService::save)
                    .then();
        if (wxOpenXmlMessage.getInfoType().equals("unauthorized"))
            return save(Notify.builder()
                    .appId(wxOpenXmlMessage.getAuthorizerAppid())
                    .infoType("unauthorized")
                    .message("取消授权")
                    .createdAt(LocalDateTime.now())
                    .build())
                    .flatMap(notify -> authorizerService.findOne(QAuthorizer.authorizer.appId.eq(wxOpenXmlMessage.getAuthorizerAppid())))
                    .flatMap(authorizer -> authorizerService.deleteById(authorizer.getId()))
                    .then();
        return Mono.empty();
    }

    @Override
    public Mono<Void> saveByWxMpXmlMessage(WxMpXmlMessage wxMpXmlMessage) {
        var event = wxMpXmlMessage.getEvent();
        var authorizerMono = authorizerService.findOne(QAuthorizer.authorizer.userName.eq(wxMpXmlMessage.getToUser()));
        return authorizerMono.map(authorizer -> {
                    var notify = Notify.builder()
                            .appId(authorizer.getAppId())
                            .infoType(event)
                            .createdAt(LocalDateTime.now())
                            .build();
                    var eventMap = Map.of(
                            "weapp_audit_success", "审核通过",
                            "weapp_audit_fail", "审核不通过",
                            "weapp_audit_delay", "审核延后"
                    );
                    if (eventMap.containsKey(event)) notify.setMessage(eventMap.get(event));
                    return notify;
                })
                .flatMap(this::save)
                .filterWhen(notify -> Mono.just(notify.getInfoType().equals("weapp_audit_success")))
                .flatMap(notify -> Mono.fromCallable(() -> wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(notify.getAppId()).releaseAudited())
                        .onErrorResume(WxErrorException.class, Mono::error)
                )
                .then();
    }
}
