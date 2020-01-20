package fast.cloud.nacos.tenant.mybatis;

import fast.cloud.nacos.tenant.context.TenantStore;
import fast.cloud.nacos.tenant.utils.ReflectHelper;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Properties;

/**
 * mybatis Tenant Interceptor
 * if tenant exist, Use context to determine which DB to router
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class TenantInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantInterceptor.class);

    private static final String SCHEMA_START = "/*mycat:schema=";

    private static final String SCHEMA_END = "*/";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        String tenant = TenantStore.getTenantId();

        if (tenant == null || "".equals(tenant)) {
            return invocation.proceed();
        }
        StatementHandler statementHandler = realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String sql = boundSql.getSql();
        //LOGGER.debug("TenantInterceptor before sql:" + sql);

        //add sql mycat hits for sql route
        //sql = "/*!mycat:schema=" + tenant + "*/" + sql;
        if (!sql.startsWith(SCHEMA_START)) {
            StringBuilder stringBuilder = new StringBuilder(sql.length() + 30);
            stringBuilder.append(SCHEMA_START);
            stringBuilder.append(tenant);
            stringBuilder.append(SCHEMA_END);
            stringBuilder.append(sql);
            sql = stringBuilder.toString();
        }

        LOGGER.debug("TenantInterceptor after sql:" + sql);
        ReflectHelper.setFieldValue(boundSql, "sql", sql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * <p>
     * 获得真正的处理对象,可能多层代理.
     * </p>
     */
    @SuppressWarnings("unchecked")
    private static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }

}
