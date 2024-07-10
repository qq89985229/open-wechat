package com.qq89985229.openwechat.controller.admin;
import com.qq89985229.openwechat.dto.WechatOpenDto;
import com.qq89985229.openwechat.dto.CommonDto;
import com.qq89985229.openwechat.entity.Authorizer;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController(value = "api/admin/authorizer")
@RequestMapping(value = "api/admin/authorizer")
public class AuthorizerController extends BaseController{

    @GetMapping
    public Mono<Page<Authorizer>> pageMono(CommonDto.PageQueryParam pageQueryParam){
        var pageRequest = PageRequest.of(pageQueryParam.getPage() - 1, pageQueryParam.getSize());
        var builder = new BooleanBuilder();
        return authorizerService.findBy(builder, q -> q.page(pageRequest));
    }

    @PostMapping(value = "set-ext")
    public Mono<Authorizer> setExt(@RequestBody Authorizer authorizer){
        return authorizerService.save(authorizer);
    }

    @PostMapping(value = "update-authorize-info")
    public Mono<Void> updateAuthorizerInfo(@RequestBody WechatOpenDto.AppId appId){
        return updateAuthorizerInfo(appId.getAppId());
    }

    @DeleteMapping
    public Mono<Void> deleteById(@RequestBody CommonDto.ByIds ids){
        return authorizerService.deleteAllById(ids.getIds());
    }
}
