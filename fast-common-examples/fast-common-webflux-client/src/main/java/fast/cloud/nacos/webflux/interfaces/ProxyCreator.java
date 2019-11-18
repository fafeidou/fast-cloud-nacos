package fast.cloud.nacos.webflux.interfaces;

/**
 * 创建代理类接口
 */
public interface ProxyCreator {
	/**
	 * 创建代理类
	 * 
	 * @param type
	 * @return
	 */
	Object createProxy(Class<?> type);
}
