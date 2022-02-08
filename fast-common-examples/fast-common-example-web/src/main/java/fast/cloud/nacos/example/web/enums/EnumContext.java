package fast.cloud.nacos.example.web.enums;

import fast.cloud.nacos.example.web.enums.commons.EnumInterface;
import fast.cloud.nacos.example.web.enums.commons.RegisterEnum;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class EnumContext {


    private static final String SCAN_PACKAGE = "fast.cloud.nacos";
    private static List<String> availableEnumProperty = new ArrayList<String>() {
        {
            Method[] methods = TypeResponse.class.getDeclaredMethods();
            for (Method m : methods) {
                add(firstLowerString(m.getName().substring(3)));
            }
        }
    };
    private static StringBuilder enumBody = new StringBuilder();
    private static Map<String, List<TypeResponse>> allEnumMap = new HashMap<>();


    @PostConstruct
    public void init() throws Exception {
        registerEnumHandler();
    }

    private static String firstUpperString(String content) {
        return content.substring(0, 1).toUpperCase() + content.substring(1);
    }

    private static String firstLowerString(String content) {
        return content.substring(0, 1).toLowerCase() + content.substring(1);
    }

    private static Map<String, Method> toMap(Method[] methods, Field[] fields) {
        List<String> fieldGetMethod = new ArrayList<>();
        for (Field field : fields) {
            fieldGetMethod.add("get" + firstUpperString(field.getName()));
        }
        Map<String, Method> methodMap = new HashMap<>();
        for (Method m : methods) {
            if (m.getName().startsWith("get") && fieldGetMethod.contains(m.getName())) {
                methodMap.put(m.getName(), m);
            }
        }
        return methodMap;
    }

    /**
     * 获取枚举值列表
     */
    public static List<TypeResponse> getEnumListByName(String enumName) {
        return allEnumMap.get(enumName);
    }

    /**
     * 批量获取枚举值列表
     */
    public static Map<String, List<TypeResponse>> getEnumListByNames(List<String> enumNames) {
        Map<String, List<TypeResponse>> map = new HashMap<>();
        enumNames.forEach(s -> map.put(s, allEnumMap.get(s)));
        return map;
    }

    private void registerEnumHandler() throws Exception {
        Reflections reflections = new Reflections(SCAN_PACKAGE);
        Set<Class<?>> typeClass = reflections.getTypesAnnotatedWith(RegisterEnum.class, true);
        for (Class<?> clazz : typeClass) {
            RegisterEnum registerInfo = clazz.getAnnotation(RegisterEnum.class);
            boolean available = EnumInterface.class.isAssignableFrom(clazz);
            if (!available) {
                throw new Error("启用注解RegisterEnum的枚举类必须实现接口EnumInterface:" + clazz.getSimpleName());
            }
            //打印枚举类信息
            logEnumInfoTr(registerInfo, clazz.getSimpleName());
            String enumName = registerInfo.value();
            List<TypeResponse> responses = new ArrayList<>();
            Method[] methods = clazz.getMethods();

            Field[] fields = clazz.getDeclaredFields();
            Map<String, Method> mapMethod = toMap(methods, fields);
            //遍历枚举单例
            for (Field field : clazz.getFields()) {
                boolean matchField = field.getType() == clazz;
                //如果字段是一个枚举类对象
                if (matchField) {
                    TypeResponse response = new TypeResponse();
                    for (String key : mapMethod.keySet()) {
                        String k = firstLowerString(key.substring(3));
                        int index = availableEnumProperty.indexOf(k);
                        //如果改字段是需要的
                        if (index != -1) {
                            //throw new Error("枚举类初始化异常,不支持的枚举属性:"+k);
                            field.setAccessible(true);
                            Object value = mapMethod.get(key).invoke(field.get(clazz));
                            Field f = TypeResponse.class.getDeclaredField(k);
                            f.setAccessible(true);
                            if ("getCode".equals(key)) {
                                f.set(response, Integer.valueOf(value.toString()));
                            } else if ("getDesc".equals(key)) {
                                f.set(response, String.valueOf(value));
                            }
                        }
                    }
                    responses.add(response);
                }
            }
            if (allEnumMap.get(enumName) != null) {
                throw new Error("枚举类NAME命名冲突:" + enumName);
            }
            allEnumMap.put(enumName, responses);
        }

    }

    private void logEnumInfoTr(RegisterEnum registerInfo, String enumName) {
        enumBody.append("<tr>")
            .append("<td>").append(registerInfo.value()).append("</td>")
            .append("<td>").append(enumName).append("</td>")
            .append("<td>").append(registerInfo.desc()).append("</td>")
            .append("<td>").append(String.join("<br>", registerInfo.usedLocation())).append("</td>")
            .append("<td>").append(String.join("<br>", registerInfo.usedUrl())).append("</td>")
            .append("</tr>");
    }
}
