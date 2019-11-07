package fast.cloud.nacos.controller;

import fast.cloud.nacos.feign.openapi.FileUploadFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Api(value = "文件上传")
@RequestMapping("/feign")
public class FeignUploadController {

    @Autowired
    private FileUploadFeignClient fileUploadFeignClient;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ApiOperation(value = "文件上传", notes = "请选择文件上传")
    public String imageUpload(@RequestPart(value = "file") FilePart file) {
        return fileUploadFeignClient.fileUpload(file);
    }

}
