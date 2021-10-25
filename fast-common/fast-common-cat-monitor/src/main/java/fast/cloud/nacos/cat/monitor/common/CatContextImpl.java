package fast.cloud.nacos.cat.monitor.common;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

public class CatContextImpl implements Cat.Context {
    private Map<String, String> properties = new HashMap(16);

    public CatContextImpl() {
    }

    @Override
    public void addProperty(String key, String value) {
        this.properties.put(key, value);
    }

    @Override
    public String getProperty(String key) {
        return this.properties.get(key);
    }
}