package fast.cloud.nacos.gatewaylimiter;

import fast.cloud.nacos.gatewaylimiter.config.RemoteAddrKeyResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayLimiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayLimiterApplication.class, args);
	}

	/**
	 * 按照Path限流
	 *
	 * @return key
	 */
//	@Bean(name = "pathKeyResolver")
//	public KeyResolver pathKeyResolver() {
//		return exchange -> Mono.just(
//				exchange.getRequest()
//						.getPath()
//						.toString()
//		);
//	}

	@Bean(name = RemoteAddrKeyResolver.BEAN_NAME)
	public RemoteAddrKeyResolver remoteAddrKeyResolver() {
		return new RemoteAddrKeyResolver();
	}
}
