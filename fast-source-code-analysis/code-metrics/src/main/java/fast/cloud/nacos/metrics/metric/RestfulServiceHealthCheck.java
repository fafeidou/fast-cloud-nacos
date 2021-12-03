package fast.cloud.nacos.metrics.metric;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author qinfuxiang
 */
public class RestfulServiceHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy("The Restful service well.");
    }
}
