package com.qq89985229.openwechat.controller.admin;
import cn.binarywang.wx.miniapp.bean.code.WxMaCodeSubmitAuditItem;
import com.alibaba.fastjson.JSONObject;
import com.qq89985229.openwechat.dto.WechatOpenDto;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.message.WxOpenMaSubmitAuditMessage;
import me.chanjar.weixin.open.bean.result.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.nio.file.Files;
import java.util.Objects;

@RestController(value = "api/admin/open-ma")
@RequestMapping(value = "api/admin/open-ma")
public class WxOpenMaController extends BaseController{
    /**
     * 获取小程序服务器域名
     */
    @GetMapping(value = "get-domain")
    public Mono<WxOpenMaDomainResult> getDomain(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> getWxOpenMaService(appId.getAppId()).getDomain())
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 配置小程序服务器域名
     */
    @PostMapping(value = "modify-domain")
    public Mono<WxOpenMaDomainResult> modifyDomain(@RequestBody WechatOpenDto.ModifyDomain modifyDomain){
        return Mono.fromCallable(() -> getWxOpenMaService(modifyDomain.getAppId()).modifyDomain("set", modifyDomain.getRequestDomain(), modifyDomain.getWsRequestDomain(), modifyDomain.getUploadDomain(), modifyDomain.getDownloadDomain(), null, null))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 获取小程序业务域名
     */
    @GetMapping(value = "get-web-view-domain")
    public Mono<WxOpenMaWebDomainResult> getWebViewDiamond(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> getWxOpenMaService(appId.getAppId()).getWebViewDomainInfo())
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 配置小程序业务域名
     */
    @PostMapping(value = "set-web-view-domain")
    public Mono<WxOpenMaWebDomainResult> setWebViewDomain(@RequestBody WechatOpenDto.SetWebViewDomain setWebViewDomain){
        return Mono.fromCallable(() -> getWxOpenMaService(setWebViewDomain.getAppId()).setWebViewDomainInfo("set", setWebViewDomain.getWebviewdomainList()))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 获取小程序的第三方提交代码的页面配置
     */
    @GetMapping(value = "get-page-list")
    public Mono<WxOpenMaPageListResult> getPageList(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> getWxOpenMaService(appId.getAppId()).getPageList())
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 为授权的小程序帐号上传小程序代码
     */
    @PostMapping(value = "code-commit")
    public Mono<WxOpenResult> codeCommit(@RequestBody WechatOpenDto.CodeCommit codeCommit) {
        var extInfo = (Objects.nonNull(codeCommit.getExt())) ? codeCommit.getExt() : new JSONObject();
        return Mono.fromCallable(() -> getWxOpenMaService(codeCommit.getAppId()).codeCommit(codeCommit.getTemplateId(), codeCommit.getUserVersion(), codeCommit.getUserDesc(), extInfo))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 获得体验者列表
     */
    @GetMapping(value = "get-tester-list")
    public Mono<WxOpenMaTesterListResult> getTesterList(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> getWxOpenMaService(appId.getAppId()).getTesterList())
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 绑定小程序体验者
     */
    @PostMapping(value = "bind-tester")
    public Mono<WxOpenMaBindTesterResult> bindTester(@RequestBody WechatOpenDto.BindTester bindTester){
        return Mono.fromCallable(() -> getWxOpenMaService(bindTester.getAppId()).bindTester(bindTester.getWechatId()))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 解除绑定小程序体验者
     */
    @PostMapping(value = "unbind-tester")
    public Mono<WxOpenResult> unbindTester(@RequestBody WechatOpenDto.UnbindTester unbindTester){
        return Mono.fromCallable(() -> getWxOpenMaService(unbindTester.getAppId()).unbindTesterByUserStr(unbindTester.getUserStr()))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     *获取体验小程序的体验二维码
     */
    @GetMapping(value = "get-test-qrcode", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getTestQrcode(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> Files.readAllBytes(getWxOpenMaService(appId.getAppId()).getTestQrcode("pages/index/index", null).toPath()))
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 将第三方提交的代码包提交审核
     */
    @PostMapping(value = "submit-audit")
    public Mono<WxOpenMaSubmitAuditResult> submitAudit(@RequestBody WechatOpenDto.AppId appId) {
        return Mono.fromCallable(() -> {
                    var categoryList = getWxOpenMaService(appId.getAppId()).getCategoryList();
                    var itemList = categoryList.getCategoryList()
                            .stream()
                            .map(wxOpenMaCategory -> {
                                var item = new WxMaCodeSubmitAuditItem();
                                item.setFirstClass(wxOpenMaCategory.getFirstClass());
                                item.setSecondClass(wxOpenMaCategory.getSecondClass());
                                item.setFirstId(Long.valueOf(wxOpenMaCategory.getFirstId()));
                                item.setSecondId(Long.valueOf(wxOpenMaCategory.getSecondId()));
                                return item;
                            })
                            .toList();
                    var message = new WxOpenMaSubmitAuditMessage();
                    message.setItemList(itemList);
                    return getWxOpenMaService(appId.getAppId()).submitAudit(message);
                })
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 小程序审核撤回
     */
    @PostMapping(value = "undo-code-audit")
    public Mono<WxOpenResult> undoCodeAudit(@RequestBody WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> getWxOpenMaService(appId.getAppId()).undoCodeAudit())
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 查询小程序版本信息
     */
    @GetMapping(value = "get-version-info")
    public Mono<WxOpenVersioninfoResult> getVersionInfo(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> getWxOpenMaService(appId.getAppId()).getVersionInfo())
                .onErrorResume(WxErrorException.class, Mono::error);
    }

    /**
     * 查询最新一次提交的审核状态
     */
    @GetMapping(value = "get-latest-audit-status")
    public Mono<WxOpenMaQueryAuditResult> getLatestAuditStatus(WechatOpenDto.AppId appId){
        return Mono.fromCallable(() -> getWxOpenMaService(appId.getAppId()).getLatestAuditStatus())
                .onErrorResume(WxErrorException.class, Mono::error);
    }

}