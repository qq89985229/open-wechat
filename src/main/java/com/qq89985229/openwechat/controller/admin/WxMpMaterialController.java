package com.qq89985229.openwechat.controller.admin;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController(value = "api/admin/mp-material")
@RequestMapping(value = "api/admin/mp-material")
public class WxMpMaterialController extends BaseController{
    /**
     * 新增临时素
     */
    @PostMapping(value = "media-upload")
    public Mono<WxMediaUploadResult> mediaUpload(@RequestPart(value = "file") Mono<FilePart> filePartMono, @RequestPart(value = "appId") String appId){
        return filePartMono
                .flatMap(filePart -> {
                    var filename = filePart.filename();
                    var tempFilePath = Paths.get("uploads", filename);
                    return filePart.transferTo(tempFilePath)
                            .thenReturn(tempFilePath)
                            .publishOn(Schedulers.boundedElastic())
                            .handle((tempFile1, sink) -> {
                                try {
                                    sink.next(getWxMpMaterialService(appId).mediaUpload("image", tempFile1.toFile()));
                                } catch (WxErrorException e) {
                                    sink.error(new RuntimeException(e));
                                } finally {
                                    try {
                                        Files.deleteIfExists(tempFile1);
                                    } catch (IOException e) {
                                        sink.error(new RuntimeException(e));
                                    }
                                }
                            });
                });
    }


    /**
     *获取声音或者图片永久素材
     */
    @GetMapping(value = "get-image", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> materialImageOrVoiceDownload(@RequestParam(value = "appId") String appId, @RequestParam(value = "mediaId") String mediaId){
        return Mono.fromCallable(() -> getWxMpMaterialService(appId).materialImageOrVoiceDownload(mediaId).readAllBytes())
                .onErrorResume(WxErrorException.class, Mono::error);
    }
}
