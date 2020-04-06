package fast.cloud.nacos.orderservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Classname SeataProperties
 * @Description TODO
 * @Date 2020/4/6 10:47
 * @Created by qinfuxiang
 */
@ConfigurationProperties("spring.cloud.alibaba.seata")
public class SeataProperties {
    private String txServiceGroup;
    public SeataProperties() {
    }
    public String getTxServiceGroup() {
        return this.txServiceGroup;
    }
    public void setTxServiceGroup(String txServiceGroup) {
        this.txServiceGroup = txServiceGroup;
    }
}

