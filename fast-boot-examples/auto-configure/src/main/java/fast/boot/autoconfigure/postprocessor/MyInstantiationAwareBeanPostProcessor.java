package fast.boot.autoconfigure.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;
import java.util.Objects;

public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    /**
     * BeanPostProcessor接口中的方法
     * 在Bean的自定义初始化方法之前执行
     * Bean对象已经存在了
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(">>postProcessBeforeInitialization");
        return bean;
    }

    /**
     * BeanPostProcessor接口中的方法
     * 在Bean的自定义初始化方法执行完成之后执行
     * Bean对象已经存在了
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("<<postProcessAfterInitialization");
        return bean;
    }

    /**
     * InstantiationAwareBeanPostProcessor中自定义的方法
     * 在方法实例化之前执行  Bean对象还没有
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("--->postProcessBeforeInstantiation");
        // 利用cglib动态代理生成对象返回
//        if (beanClass == User.class) {
//            Enhancer e = new Enhancer();
//            e.setSuperclass(beanClass);
//            e.setCallback((MethodInterceptor) (obj, method, objects, methodProxy) -> {
//                System.out.println("目标方法执行前:" + method + "\n");
//                Object object = methodProxy.invokeSuper(obj, objects);
//                System.out.println("目标方法执行后:" + method + "\n");
//                return object;
//            });
//            User user = (User) e.create();
//            // 返回代理类
//            return user;
//        }
        return null;
    }

    /**
     * InstantiationAwareBeanPostProcessor中自定义的方法
     * 在方法实例化之后执行  Bean对象已经创建出来了
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("<---postProcessAfterInstantiation");
        return true;
    }

    /**
     * InstantiationAwareBeanPostProcessor中自定义的方法
     * 可以用来修改Bean中属性的内容
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        System.out.println("<---postProcessProperties--->");
        if(bean instanceof User){
            PropertyValue value = pvs.getPropertyValue("name");
            if (Objects.nonNull(value)) {
                System.out.println("修改前name的值是:"+value.getValue());
                value.setConvertedValue("bobo");
            }
        }
        return pvs;
    }

//    @Override
//    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
//        System.out.println("<---postProcessPropertyValues--->");
//        return pvs;
//    }

    /**
     * InstantiationAwareBeanPostProcessor中自定义的方法 可以用来修改Bean中属性的内容
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
                                                    String beanName) throws BeansException {
        System.out.println("<---postProcessPropertyValues--->");
        if(bean instanceof User){
            PropertyValue value = pvs.getPropertyValue("name");
            System.out.println("修改前name的值是:"+value.getValue());
            value.setConvertedValue("bobo");
        }
        return pvs;
    }
}

