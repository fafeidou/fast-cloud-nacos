package fast.cloud.nacos.grpc.starter.fallback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FallbackActionDelegate {
    private final Map<Method, Method> fallbackMethodMap = new ConcurrentHashMap<>();

    public Method getFallbackMethod(final Object fallbackObj, final Method method) {
        if (Objects.isNull(fallbackObj)) {
            return null;
        }

        Method temp = fallbackMethodMap.get(method);
        if (temp == null) {
            temp = findMethod(fallbackObj.getClass(), method.getName(), method.getParameterTypes(), method.getReturnType());
            if (temp != null) {
                Method existingMethod = fallbackMethodMap.putIfAbsent(method, temp);
                if (existingMethod != null) {
                    temp = existingMethod;
                }
            }
            if (temp == null) {
                return null;
            }
        }
        return temp;
    }

    public Object getFallbackAction(final Object fallbackObj, final Method method) {
        final Method methodImpl = getFallbackMethod(fallbackObj, method);
        if (methodImpl == null) {
            return null;
        }
        final Object invokedObj = fallbackObj;
        methodImpl.setAccessible(true);
        try {
            return methodImpl.invoke(invokedObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Method findMethod(Class<?> clazz, String name, Class<?>[] paramTypes, Class<?> returnType) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (name.equals(method.getName()) && returnType.isAssignableFrom(method.getReturnType()) && Arrays.equals(paramTypes, method.getParameterTypes())) {
                return method;
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !Object.class.equals(superClass)) {
            return this.findMethod(superClass, name, paramTypes, returnType);
        }
        return null;
    }
}
