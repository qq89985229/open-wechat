package com.qq89985229.openwechat.controller.admin;
import com.qq89985229.openwechat.dto.WechatOpenDto;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.WxOpenMaCodeTemplate;
import me.chanjar.weixin.open.bean.result.WxOpenResult;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController(value = "api/admin/open-component")
@RequestMapping(value = "api/admin/open-component")
public class WxOpenComponentController extends BaseController{
    @Resource
    private Environment env;

    /**
     * 快速创建
     */
    @PostMapping(value = "fast-register")
    public Mono<WxOpenResult> fastRegister(@RequestBody WechatOpenDto.FastRegister fastRegister) {
        return Mono.fromCallable(() -> getWxOpenComponentService().fastRegisterWeapp(
                        fastRegister.getName(),
                        fastRegister.getCode(),
                        fastRegister.getCodeType(),
                        fastRegister.getLegalPersonaWechat(),
                        fastRegister.getLegalPersonaName(),
                        env.getProperty("wx.open-wechat.mobile")
                ))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 获取用户授权页URL
     */
    @GetMapping(value = "get-pre-auth-url")
    public Mono<String> getPreAuthUrl() {
        return Mono.fromCallable(() -> getWxOpenComponentService().getPreAuthUrl(env.getProperty("wx.open-wechat.preAuthUrl") + "/api/notify/auth/jump" ))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     *获取草稿箱内的所有临时代码草稿
     */
    @GetMapping(value = "get-template-draft-List")
    public Flux<WxOpenMaCodeTemplate> getTemplateDraftList() {
        return Mono.fromCallable(() -> getWxOpenComponentService().getTemplateDraftList())
                .flatMapMany(Flux::fromIterable)
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     将草稿箱的草稿选为小程序代码模版
     */
    @PostMapping(value = "add-to-template")
    public Mono<Void> addToTemplate(@RequestBody WechatOpenDto.AddToTemplate addToTemplate){
        return Mono.fromCallable(() -> {
            getWxOpenComponentService().addToTemplate(addToTemplate.getDraftId(), 0);
            return null;
        });
    }

    /**
     *获取代码模板列表
     */
    @GetMapping(value = "get-template-List")
    public Flux<WxOpenMaCodeTemplate> getTemplateList() {
        return Mono.fromCallable(() -> getWxOpenComponentService().getTemplateList(0))
                .flatMapMany(Flux::fromIterable)
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 删除指定小程序代码模版
     */
    @DeleteMapping(value = "delete-template")
    public Mono<Void> deleteTemplate(@RequestBody WechatOpenDto.DeleteTemplate deleteTemplate) {
        return Mono.fromCallable(() -> {
            getWxOpenComponentService().deleteTemplate(deleteTemplate.getTemplateId());
            return null;
        });
    }

}
