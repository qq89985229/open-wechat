package com.qq89985229.openwechat.controller.admin;
import com.qq89985229.openwechat.dto.WechatOpenDto;
import com.qq89985229.openwechat.entity.Privacy;
import com.qq89985229.openwechat.service.PrivacyService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.ma.privacy.GetPrivacySettingResult;
import me.chanjar.weixin.open.bean.ma.privacy.PrivacyOwnerSetting;
import me.chanjar.weixin.open.bean.ma.privacy.SetPrivacySetting;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController(value = "api/admin/open-ma-privacy")
@RequestMapping(value = "api/admin/open-ma-privacy")
public class WxOpenMaPrivacyController extends BaseController{
    @Resource
    PrivacyService privacyService;
    @Resource
    Environment env;

    /**
     * 获取小程序用户隐私保护
     */
    @GetMapping(value = "get-privacy-setting")
    public Flux<Privacy> getPrivacySetting(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> {
                    var privacySettingResult = getWxOpenMaPrivacyService(appId.getAppId()).getPrivacySetting(2);
                    var privacyKeyList = privacySettingResult.getSettingList()
                            .stream()
                            .map(GetPrivacySettingResult.Setting::getPrivacyKey)
                            .toList();
                    return privacyService.findAll()
                            .map(privacy -> {
                                if (privacyKeyList.contains(privacy.getPrivacyKey())) privacy.setChecked(Boolean.TRUE);
                                return privacy;
                            });
                })
                .flatMapMany(Flux::from)
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 设置小程序用户隐私保护
     */
    @PostMapping(value = "set-privacy-setting")
    public Mono<Void> setPrivacySetting(@RequestBody WechatOpenDto.PrivacySetting privacySetting) {
        return Mono.fromCallable(() -> {
            var wxOpenMaPrivacyService = getWxOpenMaPrivacyService(privacySetting.getAppId());
            var settingList = privacySetting.getPrivacyList()
                    .stream()
                    .map(privacy -> SetPrivacySetting.Setting
                            .builder()
                            .privacyKey(privacy.getPrivacyKey())
                            .privacyText(privacy.getPrivacyDesc())
                            .build()
                    )
                    .toList();
            var setPrivacySetting = SetPrivacySetting.builder()
                    .ownerSetting(PrivacyOwnerSetting.builder()
                            .contactPhone(env.getProperty("wx.open-wechat.mobile"))
                            .noticeMethod("通过弹窗")
                            .build()
                    )
                    .settingList(settingList)
                    .build();
            wxOpenMaPrivacyService.setPrivacySetting(setPrivacySetting);
            return null;
        });
    }
}
