package com.qq89985229.openwechat.controller.notify;
import com.qq89985229.openwechat.service.NotifyService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.Objects;

@RestController(value = "api/notify")
@RequestMapping(value = "api/notify")
public class NotifyController {
    @Resource
    WxOpenService wxOpenService;
    @Resource
    NotifyService notifyService;

    @RequestMapping(value = "receive-ticket")
    public Mono<String> receiveTicket(
            @RequestBody(required = false) String requestBody,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("signature") String signature,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        var wxOpenComponentService = wxOpenService.getWxOpenComponentService();
        if (!"aes".equalsIgnoreCase(encType) || !wxOpenComponentService.checkSignature(timestamp, nonce, signature))
            return Mono.error(new IllegalArgumentException("非法请求，可能属于伪造的请求！"));
        var wxOpenXmlMessage = WxOpenXmlMessage.fromEncryptedXml(requestBody, wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        return notifyService.saveByWxOpenXmlMessage(wxOpenXmlMessage)
                .then(Mono.fromCallable(() -> wxOpenComponentService.route(wxOpenXmlMessage)).onErrorResume(WxErrorException.class, Mono::error));
    }

    @RequestMapping("{appId}/callback")
    public Mono<String> callback(@RequestBody(required = false) String requestBody,
                                 @PathVariable("appId") String appId,
                                 @RequestParam("signature") String signature,
                                 @RequestParam("nonce") String nonce,
                                 @RequestParam("timestamp") String timestamp,
                                 @RequestParam("encrypt_type") String encType,
                                 @RequestParam("msg_signature") String msgSignature) {
        var wxOpenComponentService = wxOpenService.getWxOpenComponentService();
        if (!"aes".equalsIgnoreCase(encType) || !wxOpenComponentService.checkSignature(timestamp, nonce, signature))
            return Mono.error(new IllegalArgumentException("非法请求，可能属于伪造的请求！"));
        var wxMpXmlMessage = WxOpenXmlMessage.fromEncryptedMpXml(requestBody, wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        return notifyService.saveByWxMpXmlMessage(wxMpXmlMessage)
                .then(Mono.fromCallable(() -> {
                    if (StringUtils.equalsAnyIgnoreCase(appId, "wxbfdd1a347cfe51a2", "wx2db32121105718fe")){
                        if (StringUtils.equals(wxMpXmlMessage.getMsgType(), "text")) {
                            if (StringUtils.equals(wxMpXmlMessage.getContent(), "TESTCOMPONENT_MSG_TYPE_TEXT")) {
                                return WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(
                                        WxMpXmlOutMessage.TEXT().content("TESTCOMPONENT_MSG_TYPE_TEXT_callback")
                                                .fromUser(wxMpXmlMessage.getToUser())
                                                .toUser(wxMpXmlMessage.getFromUser())
                                                .build(),
                                        wxOpenService.getWxOpenConfigStorage()
                                );
                            } else if (StringUtils.startsWith(wxMpXmlMessage.getContent(), "QUERY_AUTH_CODE:")) {
                                var msg = wxMpXmlMessage.getContent().replace("QUERY_AUTH_CODE:", "") + "_from_api";
                                var keFuMessage = WxMpKefuMessage.TEXT().content(msg).toUser(wxMpXmlMessage.getFromUser()).build();
                                wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService().sendKefuMessage(keFuMessage);
                            }
                        } else if (StringUtils.equals(wxMpXmlMessage.getMsgType(), "event")) {
                            var keFuMessage = WxMpKefuMessage.TEXT().content(wxMpXmlMessage.getEvent() + "from_callback").toUser(wxMpXmlMessage.getFromUser()).build();
                            wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService().sendKefuMessage(keFuMessage);
                        }
                    }
                    var wxOpenMessageRouter = new WxOpenMessageRouter(wxOpenService);
                    var wxMpXmlOutMessage = wxOpenMessageRouter.route(wxMpXmlMessage, appId);
                    if(Objects.nonNull(wxMpXmlOutMessage)) return WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(wxMpXmlOutMessage, wxOpenService.getWxOpenConfigStorage());
                    return "success";
                }));
    }

    @GetMapping("auth/jump")
    public Mono<WxOpenQueryAuthResult> authJump(@RequestParam("auth_code") String authCode){
        return Mono.fromCallable(() -> wxOpenService.getWxOpenComponentService().getQueryAuth(authCode))
                .onErrorResume(WxErrorException.class, Mono::error);
    }
}
