package com.qq89985229.openwechat.service;

import com.qq89985229.openwechat.entity.Notify;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import reactor.core.publisher.Mono;

public interface NotifyService extends MongoService<Notify> {
    Mono<Void> saveByWxOpenXmlMessage(WxOpenXmlMessage wxOpenXmlMessage);
    Mono<Void> saveByWxMpXmlMessage(WxMpXmlMessage wxMpXmlMessage);
}
