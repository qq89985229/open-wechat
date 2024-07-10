package com.qq89985229.openwechat.controller.admin;
import com.qq89985229.openwechat.dto.CommonDto;
import com.qq89985229.openwechat.entity.Notify;
import com.qq89985229.openwechat.service.NotifyService;
import com.querydsl.core.BooleanBuilder;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController(value = "api/admin/notify")
@RequestMapping(value = "api/admin/notify")
public class NotifyController extends BaseController{

    @Resource
    NotifyService notifyService;

    @GetMapping
    public Mono<Page<Notify>> pageMono(CommonDto.PageQueryParam pageQueryParam){
        var pageRequest = PageRequest.of(pageQueryParam.getPage() - 1, pageQueryParam.getSize());
        var builder = new BooleanBuilder();
        return notifyService.findBy(builder, q -> q.page(pageRequest));
    }
}
