package fast.cloud.nacos.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtil {
    private static final Map<String, Set<Method>> methodCache = new ConcurrentHashMap<>();

    public static Set<Method> findCandidateMethods(Class<?>[] classes, String name) {
        StringBuilder sb = new StringBuilder();
        for (Class<?> clazz : classes) {
            sb.append(clazz.getName()).append("::");
        }
        String cacheKey = sb.append(name).toString();
        if (methodCache.containsKey(cacheKey)) {
            return methodCache.get(cacheKey);
        }
        Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(name)) {
                    methods.add(method);
                }
            }
        }
        methods = Collections.unmodifiableSet(methods);
        methodCache.put(cacheKey, methods);
        return methods;
    }
}
