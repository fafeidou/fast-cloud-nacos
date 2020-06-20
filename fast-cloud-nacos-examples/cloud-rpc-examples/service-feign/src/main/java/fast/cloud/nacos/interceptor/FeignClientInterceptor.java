package fast.cloud.nacos.interceptor;

import static fast.cloud.nacos.common.model.beans.context.ProjectContext.CONTEXT_KEY;

import fast.cloud.nacos.common.model.beans.context.ProjectContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Objects;

/**
 * @author qinfuxiang
 */
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
