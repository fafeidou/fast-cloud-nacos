package fast.cloud.nacos.example.web.config;

/**
 * @author qinfuxiang
 * @Date 2020/6/5 13:43
 */
public class HttpApiProperties {
    private boolean init;
    private int idle = 5;

    public HttpApiProperties() {
    }

    public boolean isInit() {
        return this.init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public int getIdle() {
        return this.idle;
    }

    public void setIdle(int idle) {
        this.idle = idle;
    }
}

