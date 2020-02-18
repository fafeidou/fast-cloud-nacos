package fast.cloud.nacos.interceptor;

import fast.cloud.nacos.common.model.beans.context.ProjectContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Objects;

import static fast.cloud.nacos.common.model.beans.context.ProjectContext.CONTEXT_KEY;

public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            ProjectContext projectContext = ProjectContext.getContext();
            if (Objects.nonNull(projectContext)) {
                requestTemplate.header(CONTEXT_KEY, projectContext.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
