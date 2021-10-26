package fast.cloud.k8s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootApplication

public class FastK8sClientApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(FastK8sClientApplication.class);

    private final Map<String, Integer> portMapping = new HashMap<>(16);
    private final Map<String, Set<String>> addressesMapping = new HashMap<>(16);

    public static void main(String[] args) {
        SpringApplication.run(FastK8sClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
