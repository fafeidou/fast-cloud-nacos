package fast.cloud.nacos.common.tenant.context;

import com.alibaba.ttl.TransmittableThreadLocal;

public class TenantStore implements BatmanTenant {
    private static final ThreadLocal<String> CONTEXT = new TransmittableThreadLocal<>();

    private static boolean isApplicationTenant = false;

    private static String applicationTenantId;

    private static final String TENANT_DEFAULT_ID = "t0";

    public static void setTenantId(String tenantId) {
        CONTEXT.set(tenantId);
    }

    public static String getTenantId() {
        if (isApplicationTenant) {
            return applicationTenantId;
        }

        String tenantId = CONTEXT.get();
        if (tenantId == null || "".equals(tenantId)) {
            tenantId = TENANT_DEFAULT_ID;
        }
        return tenantId;
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static boolean isApplicationTenant() {
        return isApplicationTenant;
    }

    public static void setApplicationTenant(boolean applicationTenant) {
        isApplicationTenant = applicationTenant;
    }

    public static String getApplicationTenantId() {
        return applicationTenantId;
    }

    public static void setApplicationTenantId(String applicationTenantId) {
        TenantStore.applicationTenantId = applicationTenantId;
    }

    @Override
    public void setBatmanTenantId(String s) {
        setTenantId(s);
    }

    @Override
    public String getBatmanTenantId() {
        return getTenantId();
    }
}
