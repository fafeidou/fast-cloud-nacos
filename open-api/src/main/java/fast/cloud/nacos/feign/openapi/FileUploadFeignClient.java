package fast.cloud.nacos.feign.openapi;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(value = "service-file")
public interface FileUploadFeignClient {

    /***
     * 1.produces,consumes必填
     * 2.注意区分@RequestPart和RequestParam，不要将
     * @RequestPart(value = "file") 写成@RequestParam(value = "file")
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFile/server", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String fileUpload(@RequestPart FilePart file);

}
