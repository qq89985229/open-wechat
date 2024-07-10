package com.qq89985229.openwechat.eip.openwechat;
import com.qq89985229.openwechat.entity.Authorizer;
import com.qq89985229.openwechat.entity.QAuthorizer;
import com.qq89985229.openwechat.service.AuthorizerService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.result.WxOpenAuthorizerListResult;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Component
public class AuthorizerRoute extends RouteBuilder {
    @Resource
    WxOpenService wxOpenService;
    @Resource
    AuthorizerService authorizerService;
    @Override
    public void configure() throws Exception {
/*        from("timer:initial//start?period=" + 1000L * 60L * 60L)
                .bean(wxOpenService.getWxOpenComponentService(), "getAuthorizerList(0, 500)")
                .process(exchange -> {
                    var wxOpenAuthorizerListResult = exchange.getIn().getBody(WxOpenAuthorizerListResult.class);
                    var authorizerFlux = authorizerService.findAll();
                    Flux.fromIterable(wxOpenAuthorizerListResult.getList())
                            .map(map -> Authorizer.builder()
                                    .appId(map.get("authorizer_appid"))
                                    .authTime(Long.parseLong(map.get("auth_time")))
                                    .createdAt(LocalDateTime.now())
                                    .build())
                            .filterWhen(map -> authorizerFlux
                                    .any(authorizer -> authorizer.getAppId().equals(map.getAppId()))
                                    .map(exists -> !exists)
                            )
                            .collectList()
                            .flatMapMany(Flux::fromIterable)
                            .buffer(1000)
                            .flatMap(authorizerService::saveAll)
                            .subscribe();
                });

        from("timer:initial//start?period=" + 1000L * 60L)
                .process(exchange -> authorizerService.findBy(QAuthorizer.authorizer.authorizerInfo.isNull().or(QAuthorizer.authorizer.userName.isNull()), q -> q.limit(1).first())
                        .flatMap(authorizer -> Mono.fromCallable(() -> {
                            var authorizerInfo = wxOpenService.getWxOpenComponentService().getAuthorizerInfo(authorizer.getAppId());
                            authorizer.setUserName(authorizerInfo.getAuthorizerInfo().getUserName());
                            authorizer.setAuthorizerInfo(authorizerInfo);
                            return authorizer;
                        }))
                        .onErrorResume(WxErrorException.class, Mono::error)
                        .flatMap(authorizerService::save)
                        .subscribe());*/
    }
}
