package fast.cloud.nacos.example.web.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fast.cloud.nacos.example.web.annotation.HttpApi;
import fast.cloud.nacos.example.web.annotation.HttpApiGroup;
import fast.cloud.nacos.example.web.bean.HttpApiInfo;
import fast.cloud.nacos.example.web.config.HttpApiProperties;
import fast.cloud.nacos.example.web.controller.PrivilegeController;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author qinfuxiang
 * @Date 2020/6/5 13:44
 */
@Service
public class HttpApiCollector {

    private Logger logger = LoggerFactory.getLogger(HttpApiCollector.class);
    private static final String CACHE_KEY = HttpApiCollector.class.getName();
    private LoadingCache<String, List<HttpApiInfo>> loadingCache = null;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final HttpApiProperties httpApiProperties;
    @Value("${spring.application.name}")
    private String serviceName;

    public HttpApiCollector(HttpApiProperties httpApiProperties,
        RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.httpApiProperties = httpApiProperties;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @PostConstruct
    public void initCacheLoader() throws NoSuchMethodException {
        CacheLoader<String, List<HttpApiInfo>> cacheLoader = new CacheLoader<String, List<HttpApiInfo>>() {
            @Override
            public List<HttpApiInfo> load(String key) {
                return HttpApiCollector.this.collect();
            }
        };
        this.loadingCache = CacheBuilder
            .newBuilder().concurrencyLevel(1)
            .expireAfterAccess((long) this.httpApiProperties.getIdle(), TimeUnit.MINUTES).initialCapacity(1)
            .maximumSize(1L).recordStats().removalListener((notification) -> {
                this.logger.info("### Guava缓存[{}]被移除了: {}", notification.getKey(), notification.getCause());
            }).build(cacheLoader);
        if (this.httpApiProperties.isInit()) {
            PrivilegeController privilegeController;

            try {
                privilegeController = BeanUtils
                    .instantiateClass(PrivilegeController.class.getDeclaredConstructor(this.getClass()),
                        new Object[]{this});
            } catch (NoSuchMethodException var10) {
                this.logger.error("### 实例化Bean[{}]出错了!", PrivilegeController.class.getName(), var10);
                throw var10;
            }

            String basePath = "";
            RequestMapping requestMapping = PrivilegeController.class.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                basePath = requestMapping.value()[0];
            }

            RequestMappingInfo requestMappingInfo;
            Method[] methods = PrivilegeController.class.getMethods();

            for (int i = 0; i < methods.length; ++i) {
                Method method = methods[i];
                requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    requestMappingInfo = RequestMappingInfo
                        .paths(new String[]{basePath + "/" + requestMapping.value()[0]}).build();
                    this.requestMappingHandlerMapping.registerMapping(requestMappingInfo, privilegeController, method);
                }
            }

            long start = System.currentTimeMillis();
            this.requestMappingHandlerMapping.afterPropertiesSet();
            long end = System.currentTimeMillis();
            this.logger.info("### 注册PrivilegeController到RequestMappingHandlerMapping后刷新mapping耗时：{}毫秒!", end - start);
        }

    }

    private List<HttpApiInfo> collect() {
        List<HttpApiInfo> httpApiInfos = new ArrayList();
        Iterator entryIterator = this.requestMappingHandlerMapping.getHandlerMethods().entrySet().iterator();

        while (entryIterator.hasNext()) {
            Entry<RequestMappingInfo, HandlerMethod> entry = (Entry) entryIterator.next();
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            Class<?> clazz = handlerMethod.getBeanType();
            String groupName = null;
            HttpApiGroup apiGroup = AnnotationUtils.getAnnotation(clazz, HttpApiGroup.class);
            if (apiGroup != null) {
                groupName = apiGroup.value();
            }

            Method method = handlerMethod.getMethod();
            String name = null;

            if (StringUtils.isEmpty(name)) {
                HttpApi httpApi = AnnotationUtils.findAnnotation(method, HttpApi.class);
                if (httpApi != null) {
                    name = httpApi.name();
                }
            }

            if (!StringUtils.isEmpty(name)) {
                HttpApiInfo httpApiInfo = new HttpApiInfo();
                httpApiInfo.setAuthItem(
                    this.serviceName + requestMappingInfo.getPatternsCondition().getPatterns().iterator().next()
                        .replaceAll("/", "."));
                if (groupName != null) {
                    name = groupName + name;
                }

                httpApiInfo.setName(name);
                httpApiInfos.add(httpApiInfo);
            }
        }

        return httpApiInfos;
    }

    public List<HttpApiInfo> get() throws ExecutionException {
        return this.loadingCache.get(CACHE_KEY);
    }
}

