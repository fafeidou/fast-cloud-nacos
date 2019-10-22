package fast.cloud.nacos.mybatis.bean.request;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fast.cloud.nacos.common.model.request.CommonSearchRequest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * COPYRIGHT © 2005-2018 CHARLESKEITH ALL RIGHTS RESERVED.
 *
 * @author Batman.qin
 * @create 2018-12-10 16:18
 */
public class MyBaseRequest<T> extends CommonSearchRequest<T> {

    public <ENTITY> QueryWrapper<ENTITY> getQuery(Class<ENTITY> entityClass) {
        QueryWrapper<ENTITY> queryWrapper = new QueryWrapper<>();
        //添加sort
        if (this != null && this.getSortBy() != null) {
            queryWrapper.orderBy(true, this.getSortBy().getDirection() == 1 ? true : false, this.getSortBy().getField());
        }

        //设置fields
//        if (!CollectionUtils.isEmpty(needFieldList)) {
//            org.springframework.data.mongodb.core.query.Field needFields = query.fields();
//            for (String str : needFieldList) {
//                needFields.include(str);
//            }
//
//        }
        MyBaseRequest request = this;
        if (request.getCondition() == null) {
            return queryWrapper;
        }
        Class clazz = request.getCondition().getClass();

        //获取查询类Query的所有字段,包括父类字段
        List<Field> fields = getAllFieldsWithRoot(clazz);

        for (Field field : fields) {

            //获取字段上的@QueryWord注解
            QueryCondition qw = field.getAnnotation(QueryCondition.class);
            if (qw == null) {
                continue;
            }

            // 获取字段名
            String column = qw.column();
            //如果主注解上colume为默认值"",则以field为准
            if (column.equals("")) {
                column = field.getName();
            }

            field.setAccessible(true);

            try {
                Object value = field.get(request.getCondition());
                //如果值为null,注解未标注nullable,跳过
                if (value == null && !qw.nullable()) {
                    continue;
                }

                // can be empty
                if (value != null && String.class.isAssignableFrom(value.getClass())) {
                    String s = (String) value;
                    //如果值为"",且注解未标注emptyable,跳过
                    if (s.equals("") && !qw.emptyable()) {
                        continue;
                    }
                }

                //通过注解上func属性,构建路径表达式
                column = UnderlineToHump(column);
                switch (qw.func()) {
                    case equal:
                        queryWrapper.eq(column, value);
                        break;
                    case like:
                        queryWrapper.like(column, "%" + value + "%");
                        break;
                    case gt:
                        queryWrapper.gt(column, value);
                        break;
                    case lt:
                        queryWrapper.lt(column, value);
                        break;
                    case ge:
                        queryWrapper.ge(column, value);
                        break;
                    case le:
                        queryWrapper.le(column, value);
                        break;
                    case notEqual:
                        queryWrapper.ne(column, value);
                        break;
                    case notLike:
                        queryWrapper.notLike(column, "%" + value + "%");
                        break;
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return queryWrapper;
    }

    public static String UnderlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        for (String s : a) {
            if (!para.contains("_")) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    //获取类clazz的所有Field，包括其父类的Field
    private List<Field> getAllFieldsWithRoot(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        Field[] dFields = clazz.getDeclaredFields();//获取本类所有字段
        if (null != dFields && dFields.length > 0) {
            fieldList.addAll(Arrays.asList(dFields));
        }

        // 若父类是Object，则直接返回当前Field列表
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == Object.class) {
            return Arrays.asList(dFields);
        }

        // 递归查询父类的field列表
        List<Field> superFields = getAllFieldsWithRoot(superClass);

        if (null != superFields && !superFields.isEmpty()) {
            superFields.stream().
                    filter(field -> !fieldList.contains(field)).//不重复字段
                    forEach(field -> fieldList.add(field));
        }
        return fieldList;
    }
}
