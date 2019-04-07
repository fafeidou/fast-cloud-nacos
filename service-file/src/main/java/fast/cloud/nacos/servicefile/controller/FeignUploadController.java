package fast.cloud.nacos.servicefile.controller;

import fast.cloud.nacos.feign.openapi.FileUploadFeignClient;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FeignUploadController implements FileUploadFeignClient {

    @Override
    public Mono<String> fileUpload(@RequestPart(value = "file") FilePart file) {
        return Mono.just(file.filename());
    }
}
