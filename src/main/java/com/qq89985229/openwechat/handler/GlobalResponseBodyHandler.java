package com.qq89985229.openwechat.handler;
import com.qq89985229.openwechat.entity.Result;
import org.springframework.lang.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public class GlobalResponseBodyHandler extends ResponseBodyResultHandler {

    private static final MethodParameter METHOD_PARAMETER_MONO_COMMON_RESULT;

    static {
        try {
            METHOD_PARAMETER_MONO_COMMON_RESULT = new MethodParameter(
                    GlobalResponseBodyHandler.class.getDeclaredMethod("methodForParams"), -1);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public GlobalResponseBodyHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
        super(writers, resolver);
    }

    @Override
    @NonNull
    public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, HandlerResult result) {
        var body = result.getReturnValue();

        if (body instanceof Mono<?> monoBody) {
            return monoBody
                    .map(value -> (value instanceof byte[] || value instanceof Result) ? value : Result.success(value))
                    .defaultIfEmpty(Result.success(null))
                    .flatMap(value -> {
                        var bodyParameter = value instanceof byte[] ? result.getReturnTypeSource(): METHOD_PARAMETER_MONO_COMMON_RESULT;
                        return super.writeBody(Mono.just(value), bodyParameter, exchange);
                    });
        }
        if (body instanceof Flux<?> fluxBody) {
            var wrappedBody = fluxBody
                    .collectList()
                    .map(Result::success)
                    .defaultIfEmpty(Result.success(null));
            return super.writeBody(wrappedBody, METHOD_PARAMETER_MONO_COMMON_RESULT, exchange);
        }
        return this.writeBody(body, METHOD_PARAMETER_MONO_COMMON_RESULT, exchange);
    }

    private static Mono<Result<?>> methodForParams() {
        return null;
    }

}