package com.qq89985229.openwechat.config;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.util.http.apache.DefaultApacheHttpClientBuilder;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisTemplateConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class WxOpenConfig {
    @Resource
    private Environment env;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Bean
    public WxOpenService wxOpenService(){
        var redisConfigStorage =  new WxOpenInRedisTemplateConfigStorage(stringRedisTemplate, "");
        redisConfigStorage.autoRefreshToken();
        redisConfigStorage.setComponentAppId(env.getProperty("wx.open-wechat.componentAppId"));
        redisConfigStorage.setComponentAppSecret(env.getProperty("wx.open-wechat.componentSecret"));
        redisConfigStorage.setComponentToken(env.getProperty("wx.open-wechat.componentToken"));
        redisConfigStorage.setComponentAesKey(env.getProperty("wx.open-wechat.componentAesKey"));
        var clientBuilder = DefaultApacheHttpClientBuilder.get();
        clientBuilder.setSoTimeout(30000);
        redisConfigStorage.setApacheHttpClientBuilder(clientBuilder);
        var wxOpenService = new WxOpenServiceImpl();
        wxOpenService.setWxOpenConfigStorage(redisConfigStorage);
        return wxOpenService;
    }

}
