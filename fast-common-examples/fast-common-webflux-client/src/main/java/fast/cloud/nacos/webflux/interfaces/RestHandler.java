package fast.cloud.nacos.webflux.interfaces;

import fast.cloud.nacos.webflux.beans.MethodInfo;
import fast.cloud.nacos.webflux.beans.ServerInfo;

/**
 * rest请求调用handler
 *
 */
public interface RestHandler {

    /**
     * 初始化服务器信息
     *
     * @param serverInfo
     */
    void init(ServerInfo serverInfo);

    /**
     * 调用rest请求, 返回接口
     *
     * @param methodInfo
     * @return
     */
    Object invokeRest(MethodInfo methodInfo,ServerInfo serverInfo);

}
