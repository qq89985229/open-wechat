package com.qq89985229.openwechat.config;
import com.qq89985229.openwechat.util.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.server.WebFilter;
import java.util.Objects;

@Configuration
@EnableWebFluxSecurity
public class WebfluxSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public WebFilter openWeChatFilter(){
        return (exchange, chain) -> {
            var token = exchange.getRequest().getHeaders().getFirst("token");
            if (Objects.nonNull(token)){
                var jwtVerify = JwtUtils.verify(token);
                if(!jwtVerify) throw new RuntimeException("token出错了，请重新登录");
                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(new UsernamePasswordAuthenticationToken(null, null, null)));
            }
            return chain.filter(exchange);
        };
    }

    @Bean
    public SecurityWebFilterChain OpenWeChatHttpSecurity(ServerHttpSecurity httpSecurity) {
        httpSecurity.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/admin/**"))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange((exchanges) -> exchanges
                        .pathMatchers("/api/admin/login/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((swe, e) -> {
                            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return swe.getResponse().setComplete();
                        })
                        .accessDeniedHandler((swe, e) -> {
                            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return swe.getResponse().setComplete();
                        })
                )
                .addFilterAt(openWeChatFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return httpSecurity.build();
    }
}
