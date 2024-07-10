package com.qq89985229.openwechat.controller.admin;
import com.alibaba.fastjson.JSONObject;
import com.qq89985229.openwechat.dto.WechatOpenDto;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.result.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.List;

@RestController(value = "api/admin/open-ma-basic")
@RequestMapping(value = "api/admin/open-ma-basic")
public class WxOpenMaBasicController extends BaseController{

    /**
     * 获取小程序的信息
     */
    @GetMapping(value = "get-account-basic-info")
    public Mono<WxFastMaAccountBasicInfoResult> getAccountBasicInfo(WechatOpenDto.AppId appId) {
        return Mono.fromCallable(() -> getWxOpenMaBasicService(appId.getAppId()).getAccountBasicInfo());
    }

    /**
     * 小程序名称检测
     */
    @PostMapping(value = "check-nickname")
    public Mono<WxFastMaCheckNickameResult> checkNickname(@RequestBody WechatOpenDto.NicknameParam param){
        return Mono.fromCallable(() -> getWxOpenMaBasicService(param.getAppId()).checkWxVerifyNickname(param.getNickname()))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 小程序名称设置及改名
     */
    @PostMapping(value = "set-nickname")
    public Mono<WxFastMaSetNickameResult> setNickname(@RequestBody WechatOpenDto.NicknameParam param){
        return Mono.fromCallable(() -> getWxOpenMaBasicService(param.getAppId())
                        .setNickname(
                                param.getNickname(),
                                param.getIdCard(),
                                param.getLicense(),
                                param.getNamingOtherStuff1(),
                                param.getNamingOtherStuff2()
                        )
                )
                .onErrorResume(WxErrorException.class, Mono::error)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(result -> updateAuthorizerInfo(param.getAppId()).subscribe());
    }

    /**
     *修改头像
     */
    @PostMapping(value = "modify-head-image")
    public Mono<WxOpenResult> modifyHeadImage(@RequestBody WechatOpenDto.ModifyHeadImageParam param){
        return Mono.fromCallable(() -> getWxOpenMaBasicService(param.getAppId()).modifyHeadImage(param.getMediaId(), 0,0,1,1))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 修改小程序功能介绍
     */
    @PostMapping(value = "modify-signature")
    public Mono<WxOpenResult> modifySignature(@RequestBody WechatOpenDto.ModifySignatureParam param){
        return Mono.fromCallable(() -> getWxOpenMaBasicService(param.getAppId()).modifySignature(param.getSignature()))
                .onErrorResume(WxErrorException.class, Mono::error);
    }


    /**
     *获取可设置的所有类目
     */
    @GetMapping(value = "get-all-category")
    public Mono<JSONObject> getAllCategory(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> JSONObject.parseObject(getWxOpenMaBasicService(appId.getAppId()).getAllCategories()))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     *获取类目
     */
    @GetMapping(value = "get-category")
    public Mono<WxFastMaBeenSetCategoryResult> getCategory(@RequestParam(value = "appId") String appId){
        return Mono.fromCallable(() -> getWxOpenMaBasicService(appId).getCategory())
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 添加类目
     */
    @PostMapping(value = "add-category")
    public Mono<WxOpenResult> addCategory(@RequestBody WechatOpenDto.AddCategory addCategory){
        var categoryList = List.of(addCategory.getCategory());
        return Mono.fromCallable(() -> getWxOpenMaBasicService(addCategory.getAppId()).addCategory(categoryList))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 删除类目
     */
    @DeleteMapping(value = "delete-category")
    public Mono<WxOpenResult> deleteCategory(@RequestBody WechatOpenDto.DeleteCategory deleteCategory){
        return Mono.fromCallable(() -> getWxOpenMaBasicService(deleteCategory.getAppId()).deleteCategory(deleteCategory.getFirst(), deleteCategory.getSecond()))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

}
