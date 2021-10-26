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
        //直接写config path
//        getAllPos();
//        String kubeConfigPath = "config.yml";
//
//        InputStream resourceAsStream = new ClassPathResource("config.yml").getInputStream();
//        //加载k8s, config
//        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new InputStreamReader(resourceAsStream, "UTF-8"))).build();
//        Configuration.setDefaultApiClient(client);
//
//        registerWatchersIfNecessary("bfe-device-id-svc");
//
//        Set<String> address = addressesMapping.getOrDefault("bfe-device-id-svc", Collections.emptySet());
//
//        Integer port = portMapping.get("bfe-device-id-svc");
//
//        System.out.println();
        // 3. get pod meta by pod label
//        Map<String, Map<String, String>> appPodMeta = infoFetcher.getPodMeta(appId);
//
//        appPodLabels.getOrDefault(appId, new HashMap<>())
//
//        // 4. join the results of step 2 and 3 to figure out final results.
//        List<ServiceNode> serviceNodes = new ArrayList<>();
//        for (String address : appEndpoints.getAddresses()) {
//            Map<String, String> addressMeta = appPodMeta.getOrDefault(address, new HashMap<>());
//            serviceNodes.add(new ServiceNode(new cn.lalaframework.router.ServiceInstance("k8s", address, appEndpoints.getPort(), addressMeta),
//                    addressMeta));
//        }
    }
//
//    private static final String K8S_FACADE_SVC_SUFFIX = "-all";
//    private static final String FIELD_KEY = "metadata.name";
//
//
//    private void getAllPos() throws IOException, ApiException {
//        //直接写config path
//        String kubeConfigPath = "config.yml";
//
//        InputStream resourceAsStream = new ClassPathResource("config.yml").getInputStream();
//        //加载k8s, config
//        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new InputStreamReader(resourceAsStream, "UTF-8"))).build();
//
//        //将加载config的client设置为默认的client
//        Configuration.setDefaultApiClient(client);
//
//        //创建一个api
//        CoreV1Api api = new CoreV1Api();
//
//        //打印所有的pod
//        V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null,
//                null, null);
//
//        for (V1Pod item : list.getItems()) {
//            System.out.println(item);
//        }
//    }
//
//
//    public void registerWatchersIfNecessary(String appId) {
//        CoreV1Api apiHandle = new CoreV1Api();
//        SharedInformerFactory factory = new SharedInformerFactory();
//
//        SharedIndexInformer<V1Endpoints> endpointsInformer =
//                factory.sharedIndexInformerFor(
//                        (CallGeneratorParams params) -> apiHandle.listEndpointsForAllNamespacesCall(false, null,
//                                String.format("%s=%s", FIELD_KEY, k8sFacadeService(appId)), null, null, null,
//                                params.resourceVersion, params.timeoutSeconds, params.watch, null),
//                        V1Endpoints.class,
//                        V1EndpointsList.class);
//
//        endpointsInformer.addEventHandler(new ResourceEventHandler<V1Endpoints>() {
//            @Override
//            public void onAdd(V1Endpoints v1Endpoints) {
//                logger.info("A service endpoint creation event was detected for appId '{}'.", appId);
//                onEndpointsEvent(appId, v1Endpoints, null);
//            }
//
//            @Override
//            public void onUpdate(V1Endpoints v1Endpoints, V1Endpoints newV1Endpoints) {
//                logger.info("A service endpoint update event was detected for appId '{}'.", appId);
//                onEndpointsEvent(appId, newV1Endpoints, v1Endpoints);
//            }
//
//            @Override
//            public void onDelete(V1Endpoints v1Endpoints, boolean b) {
//                logger.info("A service endpoint deletion event was detected for appId '{}'.", appId);
//                onEndpointsEvent(appId, null, v1Endpoints);
//            }
//        });
//    }
//
//    private static String k8sFacadeService(final String appId) {
//        return appId + K8S_FACADE_SVC_SUFFIX;
//    }
//
//    private synchronized void onEndpointsEvent(String appId, V1Endpoints newEndpoints, V1Endpoints oldEndpoints) {
//        Set<String> newAddresses = new HashSet<>(addressesMapping.getOrDefault(appId, Collections.emptySet()));
//        if (oldEndpoints != null) {
//            if (oldEndpoints.getSubsets().size() > 1) {
//                logger.debug("Multiple subsets found in current endpoints list, apply the first group only.");
//            }
//            V1EndpointSubset subset = oldEndpoints.getSubsets().get(0);
//            subset.getAddresses().stream().forEach(e -> newAddresses.remove(e.getIp()));
//        }
//
//        Integer port = null;
//        if (newEndpoints != null && newEndpoints.getSubsets() != null) {
//            if (newEndpoints.getSubsets().size() > 1) {
//                logger.debug("Multiple subsets found in current endpoints list, apply the first group only.");
//            }
//
//            V1EndpointSubset subset = newEndpoints.getSubsets().get(0);
//            subset.getAddresses().stream().forEach(e -> newAddresses.add(e.getIp()));
//            if (subset.getPorts().size() > 1) {
//                logger.debug("Multiple ports found in current subset, apply the first one as default traffic port.");
//            }
//            port = subset.getPorts().get(0).getPort();
//        }
//
//        if (port != null) {
//            portMapping.put(appId, port);
//        }
//        addressesMapping.put(appId, newAddresses);
//    }
}
