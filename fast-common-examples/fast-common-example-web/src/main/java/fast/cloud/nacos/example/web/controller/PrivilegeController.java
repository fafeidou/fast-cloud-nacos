package fast.cloud.nacos.example.web.controller;

import fast.cloud.nacos.common.model.model.CommonCode;
import fast.cloud.nacos.common.model.response.ApiResponse;
import fast.cloud.nacos.example.web.bean.HttpApiInfo;
import fast.cloud.nacos.example.web.service.HttpApiCollector;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinfuxiang
 * @Date 2020/6/5 13:48
 */
@ConditionalOnBean({HttpApiCollector.class})
@RestController
@RequestMapping({"manage/privilege"})
public class PrivilegeController {

    private Logger logger = LoggerFactory.getLogger(PrivilegeController.class);
    private final HttpApiCollector httpApiCollector;

    public PrivilegeController(HttpApiCollector httpApiCollector) {
        this.httpApiCollector = httpApiCollector;
    }

    @RequestMapping({"collect"})
    public ApiResponse<List<HttpApiInfo>> collect() {
        try {
            return new ApiResponse(this.httpApiCollector.get());
        } catch (ExecutionException var2) {
            this.logger.error("获取权限出错了!", var2);
            return new ApiResponse<>(CommonCode.FAIL);
        }
    }

    @RequestMapping({"export"})
    @ResponseBody
    public void ResponseEntity(HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        ServletOutputStream out = null;
        ByteArrayInputStream is = null;

        try {
            List<HttpApiInfo> privileges = this.httpApiCollector.get();

            int len;
            for (len = 0; len < privileges.size(); ++len) {
                HttpApiInfo httpApiInfo = (HttpApiInfo) privileges.get(len);
                sb.append(httpApiInfo.getAuthItem()).append(" ").append(httpApiInfo.getName());
                if (len != privileges.size() - 1) {
                    sb.append("\r\n");
                }
            }

            is = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
            response.setContentType("multipart/form-data");
            response.addHeader("Content-Disposition",
                "attachment; filename=\"" + new String("privilege.txt".getBytes(StandardCharsets.UTF_8),
                    StandardCharsets.ISO_8859_1) + "\"");
            out = response.getOutputStream();
            byte[] buffer = new byte[2048];

            while ((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            out.flush();
        } catch (Exception var16) {
            this.logger.error("导出权限异常!", var16);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (is != null) {
                    is.close();
                }
            } catch (IOException var15) {
                this.logger.error("关闭流出现异常!", var15);
            }

        }

    }
}
