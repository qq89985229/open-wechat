package com.qq89985229.openwechat.controller.admin;

import com.qq89985229.openwechat.dto.CommonDto;
import com.qq89985229.openwechat.entity.Privacy;
import com.qq89985229.openwechat.service.PrivacyService;
import com.querydsl.core.BooleanBuilder;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController(value = "api/admin/privacy")
@RequestMapping(value = "api/admin/privacy")
public class PrivacyController extends BaseController{
    @Resource
    PrivacyService privacyService;

    @GetMapping
    public Mono<Page<Privacy>> pageMono(CommonDto.PageQueryParam pageQueryParam){
        var pageRequest = PageRequest.of(pageQueryParam.getPage() - 1, pageQueryParam.getSize());
        var builder = new BooleanBuilder();
        return privacyService.findBy(builder, q -> q.page(pageRequest));
    }

    @PostMapping
    public Mono<Privacy> save(@RequestBody Privacy privacy){
        return privacyService.save(privacy);
    }

    @DeleteMapping
    public Mono<Void> deleteById(@RequestBody CommonDto.ByIds ids){
        return privacyService.deleteAllById(ids.getIds());
    }
}
