package fast.cloud.nacos.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.util.NoCloseOutputStream;
import fast.cloud.nacos.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

@Slf4j
public class CustomRpcServer {
    private final ObjectMapper mapper;
    private final Class<?> remoteInterface;
    private final Object handler;

    public CustomRpcServer(ObjectMapper mapper, Object handler, Class<?> remoteInterface) {
        this.mapper = mapper;
        this.remoteInterface = remoteInterface;
        this.handler = handler;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        Class<?>[] handlerInterfaces = getHandlerInterfaces(null);
        Set<Method> methods = ReflectionUtil.findCandidateMethods(handlerInterfaces, "test");
        //赋值参数

        //TODO 根据参数找到方法名
        JsonNode result = invoke(handler, methods.iterator().next());
        writeAndFlushValue(response.getOutputStream(), result);

    }

    private void writeAndFlushValue(OutputStream output, JsonNode value) throws IOException {
        log.info("Response: {}", value);

        mapper.writeValue(new NoCloseOutputStream(output), value);
        output.write('\n');
    }

    private JsonNode invoke(Object target, Method method) throws IOException, IllegalAccessException, InvocationTargetException {
        log.info("Invoking method: {}", method.getName());

        Object result;

        result = method.invoke(target);

        log.info("Invoked method: {}, result {}", method.getName(), result);

        return hasReturnValue(method) ? mapper.valueToTree(result) : null;
    }

    private boolean hasReturnValue(Method m) {
        return m.getGenericReturnType() != null;
    }

    protected Class<?>[] getHandlerInterfaces(final String serviceName) {
        if (remoteInterface != null) {
            return new Class<?>[]{remoteInterface};
        } else if (Proxy.isProxyClass(handler.getClass())) {
            return handler.getClass().getInterfaces();
        } else {
            return new Class<?>[]{handler.getClass()};
        }
    }
}
