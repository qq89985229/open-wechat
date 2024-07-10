package com.qq89985229.openwechat.controller.admin;
import com.qq89985229.openwechat.dto.WechatOpenDto;
import com.qq89985229.openwechat.util.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController(value = "api/admin/login")
@RequestMapping(value = "api/admin/login")
public class LoginController extends BaseController {
    @Resource
    Environment env;

    @PostMapping
    public Mono<String> Login(@RequestBody WechatOpenDto.LoginParam loginParam) {
        return Mono.fromCallable(() -> {
            if (!loginParam.getUsername().equalsIgnoreCase(env.getProperty("admin.username")) && !loginParam.getPassword().equalsIgnoreCase(env.getProperty("admin.password")))
                throw new RuntimeException("账号密码错误");
            return JwtUtils.create(Map.of("username", loginParam.getUsername()), 1000L * 60L * 60L * 24L * 30L);
        });

    }
}
