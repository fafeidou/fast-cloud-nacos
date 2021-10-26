package fast.cloud.k8s;

import com.google.gson.Gson;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.*;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * k8s客户端
 *
 * @author wanghuidong
 * @date 2021/6/18 14:14
 */
@Slf4j
public class K8sClient {

    /**
     * k8s-api客户端
     */
    private ApiClient apiClient;

    private AppsV1Api appsV1Api;

    private ExtensionsV1beta1Api extensionsV1beta1Api;

    private RbacAuthorizationV1Api rbacAuthorizationV1Api;

    private CoreV1Api coreV1Api;
    /**
     * 构建集群POD内通过SA访问的客户端
     * loading the in-cluster config, including:
     * 1. service-account CA
     * 2. service-account bearer-token
     * 3. service-account namespace
     * 4. master endpoints(ip, port) from pre-set environment variables
     */
    public K8sClient() {
        try {
            this.apiClient = ClientBuilder.cluster().build();
            this.coreV1Api = new CoreV1Api(apiClient);
            this.appsV1Api = new AppsV1Api(apiClient);
            rbacAuthorizationV1Api = new RbacAuthorizationV1Api(apiClient);
            extensionsV1beta1Api = new ExtensionsV1beta1Api(apiClient);
        } catch (IOException e) {
            log.error("构建K8s-Client异常", e);
            throw new RuntimeException("构建K8s-Client异常");
        }
    }

    /**
     * 构建集群外通过UA访问的客户端
     * loading the out-of-cluster config, a kubeconfig from file-system
     *
     * @param kubeConfigPath kube连接配置文件
     */
    public K8sClient(String kubeConfigPath) {
        try {
            this.apiClient = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
            this.coreV1Api = new CoreV1Api(apiClient);
            this.appsV1Api = new AppsV1Api(apiClient);
            rbacAuthorizationV1Api = new RbacAuthorizationV1Api(apiClient);
            extensionsV1beta1Api = new ExtensionsV1beta1Api(apiClient);
        } catch (IOException e) {
            log.error("读取kubeConfigPath异常", e);
            throw new RuntimeException("读取kubeConfigPath异常");
        } catch (Exception e) {
            log.error("构建K8s-Client异常", e);
            throw new RuntimeException("构建K8s-Client异常");
        }
    }

    public void get() {
        CoreV1Api api = new CoreV1Api(apiClient);
    }

    /**
     * 获取所有的Pod
     *
     * @return podList
     */
    public V1PodList getAllPodList() {
        // new a CoreV1Api
        CoreV1Api api = new CoreV1Api(apiClient);

        // invokes the CoreV1Api client
        try {
            V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            return list;
        } catch (ApiException e) {
            log.error("获取podlist异常:" + e.getResponseBody(), e);
        }
        return null;
    }

    public V1ServiceList getAllServiceList() {
        // new a CoreV1Api
        CoreV1Api api = new CoreV1Api(apiClient);

        // invokes the CoreV1Api client
        try {
            V1ServiceList v1ServiceList = api.listServiceForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            return v1ServiceList;
        } catch (ApiException e) {
            log.error("获取podlist异常:" + e.getResponseBody(), e);
        }
        return null;
    }

    /**
     * 创建k8s service
     *
     * @param namespace   命名空间
     * @param serviceName 服务名称
     * @param port        服务端口号（和目标pod的端口号一致）
     * @param selector    pod标签选择器
     * @return 创建成功的service对象
     */
    public V1Service createService(String namespace, String serviceName, Integer port, Map<String, String> selector) {
        //构建service的yaml对象
        V1Service svc = new V1ServiceBuilder()
                .withNewMetadata()
                .withName(serviceName)
                .endMetadata()
                .withNewSpec()
                .addNewPort()
                .withProtocol("TCP")
                .withPort(port)
                .withTargetPort(new IntOrString(port))
                .endPort()
                .withSelector(selector)
                .endSpec()
                .build();

        // Deployment and StatefulSet is defined in apps/v1, so you should use AppsV1Api instead of CoreV1API
        CoreV1Api api = new CoreV1Api(apiClient);
        V1Service v1Service = null;
        try {
            v1Service = api.createNamespacedService(namespace, svc, null, null, null);
        } catch (ApiException e) {
            log.error("创建service异常:" + e.getResponseBody(), e);
        } catch (Exception e) {
            log.error("创建service系统异常:", e);
        }
        return v1Service;
    }

    /**
     * 创建k8s V1Ingress
     *
     * @param namespace   命名空间
     * @param ingressName ingress名称
     * @param annotations ingress注解
     * @param path        匹配的路径
     * @param serviceName 路由到的服务名称
     * @param servicePort 路由到的服务端口
     * @return 创建成功的ingress对象
     */
    public V1Ingress createV1Ingress(String namespace, String ingressName, Map<String, String> annotations, String path,
                                     String serviceName, Integer servicePort) {
        //构建ingress的yaml对象
        V1Ingress ingress = new V1IngressBuilder()
                .withNewMetadata()
                .withName(ingressName)
                .withAnnotations(annotations)
                .endMetadata()
                .withNewSpec()
                .addNewRule()
                .withHttp(new V1HTTPIngressRuleValueBuilder().addToPaths(new V1HTTPIngressPathBuilder()
                        .withPath(path)
                        .withPathType("Prefix")
                        .withBackend(new V1IngressBackendBuilder()
                                .withService(new V1IngressServiceBackendBuilder()
                                        .withName(serviceName)
                                        .withPort(new V1ServiceBackendPortBuilder()
                                                .withNumber(servicePort).build()).build()).build()).build()).build())
                .endRule()
                .endSpec()
                .build();

        //调用对应的API执行创建ingress的操作
        NetworkingV1Api api = new NetworkingV1Api(apiClient);
        V1Ingress v1Ingress = null;
        try {
            v1Ingress = api.createNamespacedIngress(namespace, ingress, null, null, null);
        } catch (ApiException e) {
            log.error("创建ingress异常:" + e.getResponseBody(), e);
        } catch (Exception e) {
            log.error("创建ingress系统异常:", e);
        }
        return v1Ingress;
    }


    /**
     * 创建k8s ExtensionIngress
     *
     * @param namespace   命名空间
     * @param ingressName ingress名称
     * @param annotations ingress注解
     * @param path        匹配的路径
     * @param serviceName 路由到的服务名称
     * @param servicePort 路由到的服务端口
     * @return 创建成功的ingress对象
     */
    public ExtensionsV1beta1Ingress createExtensionIngress(String namespace, String ingressName, Map<String, String> annotations, String path,
                                                           String serviceName, Integer servicePort) {
        //构建ingress的yaml对象
        ExtensionsV1beta1Ingress ingress = new ExtensionsV1beta1IngressBuilder()
                .withNewMetadata()
                .withName(ingressName)
                .withAnnotations(annotations)
                .endMetadata()
                .withNewSpec()
                .addNewRule()
                .withHttp(new ExtensionsV1beta1HTTPIngressRuleValueBuilder().addToPaths(new ExtensionsV1beta1HTTPIngressPathBuilder()
                        .withPath(path)
                        .withBackend(new ExtensionsV1beta1IngressBackendBuilder()
                                .withServiceName(serviceName)
                                .withServicePort(new IntOrString(servicePort)).build()).build()).build())
                .endRule()
                .endSpec()
                .build();

        //调用对应的API执行创建ingress的操作
        ExtensionsV1beta1Api api = new ExtensionsV1beta1Api(apiClient);
        ExtensionsV1beta1Ingress extensionsV1beta1Ingress = null;
        try {
            extensionsV1beta1Ingress = api.createNamespacedIngress(namespace, ingress, null, null, null);
        } catch (ApiException e) {
            log.error("创建ingress异常:" + e.getResponseBody(), e);
        } catch (Exception e) {
            log.error("创建ingress系统异常:", e);
        }
        return extensionsV1beta1Ingress;
    }


//    public <T> T getApiServerResponse(String namespace, Class<T> t) throws ApiException {
//        if (t == V1PodList.class) {
//            V1PodList v1PodList = api.listNamespacedPod(namespace, true, null, null, null, null, null, null, null, null);
//            return (T) v1PodList;
//        } else if (t == V1DeploymentList.class) {
//            V1DeploymentList v1DeploymentList = appsV1Api.listNamespacedDeployment(namespace, true, null, null, null, null, null, null, null, null);
//            return (T) v1DeploymentList;
//        } else if (t == V1beta1IngressList.class) {
//            V1beta1IngressList v1beta1IngressList = extensionsV1beta1Api.listNamespacedIngress(namespace, true, null, null, null, null, null, null, null, null);
//            return (T) v1beta1IngressList;
//        } else if (t == V1ServiceList.class) {
//            V1ServiceList v1ServiceList = api.listNamespacedService(namespace, true, null, null, null, null, null, null, null, null);
//            return (T) v1ServiceList;
//        } else if(t == V1ConfigMapList.class) {
//            V1ConfigMapList v1ConfigMapList = api.listNamespacedConfigMap(namespace, true, null, null, null, null, null, null, null, null);
//            return (T) v1ConfigMapList;
//        } else if(t == V1beta1NetworkPolicyList.class) {
//            V1beta1NetworkPolicyList v1beta1NetworkPolicyList = extensionsV1beta1Api.listNamespacedNetworkPolicy(namespace, true, null, null, null, null, null, null, null, null);
//            return (T) v1beta1NetworkPolicyList;
//        } else if(t == V1NodeList.class) {
//            V1NodeList v1NodeList = api.listNode(null,"true",null,null,null,null,null,null,null);
//            return (T) v1NodeList;
//        } else if(t == V1NamespaceList.class) {
//            V1NamespaceList v1NamespaceList = api.listNamespace(null,"true",null,null,null,null,null,null,null );
//            return (T) v1NamespaceList;
//        }
//        return null;
//    }
//
//    public <T> T getApiServerAllResponse(Class<T> t) throws ApiException {
//        if (t == V1PodList.class) {
//            V1PodList v1PodList = api.listPodForAllNamespaces(null,null,true,null,null,"true",null,null,null);
//            return (T) v1PodList;
//        } else if(t == V1ServiceList.class) {
//            V1ServiceList v1ServiceList = api.listServiceForAllNamespaces(null,null,true,null,null,"true",null,null,null);
//            return (T) v1ServiceList;
//        } else if(t == V1EndpointsList.class) {
//            V1EndpointsList v1EndpointsList = api.listEndpointsForAllNamespaces(null,null,true,null,null,"true",null,null,null);
//            return (T) v1EndpointsList;
//        } else if(t == V1ConfigMapList.class) {
//            V1ConfigMapList v1ConfigMapList = api.listConfigMapForAllNamespaces(null,null,true,null,null,"true",null,null,null);
//            return (T) v1ConfigMapList;
//        } else if(t == V1DeploymentList.class) {
//            V1DeploymentList v1DeploymentList = appsV1Api.listDeploymentForAllNamespaces(null,null,true,null,null,"true",null,null,null);
//            return (T) v1DeploymentList;
//        } else if(t == V1beta1IngressList.class) {
//            V1beta1IngressList v1beta1IngressList = extensionsV1beta1Api.listIngressForAllNamespaces(null,null,true,null,null,"true",null,null,null);
//            return (T) v1beta1IngressList;
//        } else if(t == V1beta1NetworkPolicyList.class) {
//            V1beta1NetworkPolicyList v1beta1NetworkPolicyList = extensionsV1beta1Api.listNetworkPolicyForAllNamespaces(null,null,true,null,null,"true",null,null,null);
//            return (T) v1beta1NetworkPolicyList;
//        }
//        return null;
//    }

    public String getApiServerAllNamespaceResponse(String type) {
        try {
            String result = "";
            Gson gson = new Gson();
            switch (type) {
                case "namespace":
                    V1NamespaceList v1NamespaceList = coreV1Api.listNamespace(null,true,null,null,null,null,null,null,null ,null);
                    result = gson.toJson(v1NamespaceList);
                    break;
                case "node":
                    V1NodeList v1NodeList = coreV1Api.listNode(null,null,null,null,null,null,null,null,null,null);
                    result = gson.toJson(v1NodeList);
                    break;
                case "pod":
                    V1PodList v1PodList = coreV1Api.listPodForAllNamespaces(null,null,null,null,null,null,null,null,null,null);
                    result = gson.toJson(v1PodList);
                    break;
                case "service":
                    V1ServiceList v1ServiceList = coreV1Api.listServiceForAllNamespaces(null,null,null,null,null,null,null,null,null,null);
                    result = gson.toJson(v1ServiceList);
                    break;
                case "endPoints":
                    V1EndpointsList v1EndpointsList = coreV1Api.listEndpointsForAllNamespaces(null,null,null,null,null,null,null,null,null, null);
                    result = gson.toJson(v1EndpointsList);
                    break;
                case "configMap":
                    V1ConfigMapList v1ConfigMapList = coreV1Api.listConfigMapForAllNamespaces(null,null,null,null,null,null,null,null,null,null);
                    result = gson.toJson(v1ConfigMapList);
                    break;
                case "deployment":
                    V1DeploymentList v1DeploymentList = appsV1Api.listDeploymentForAllNamespaces(true,null,null,null,null,null,null,null,null,null);
                    result = gson.toJson(v1DeploymentList);
                    break;
                case "ingress":
                    ExtensionsV1beta1IngressList v1beta1IngressList = extensionsV1beta1Api.listIngressForAllNamespaces(true, null, null, null, null, null, null, null, null, null);
                    result = gson.toJson(v1beta1IngressList);
                    break;
                default:
                    System.out.println("输入参数不正确");
            }
            return result;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<V1Service> getServiceByName(String serviceId) throws ApiException {
        List<V1Service> items = coreV1Api.listServiceForAllNamespaces(null, null, "metadata.name=" + serviceId,
                null, null, null, null, null, null, null).getItems();
        return items;
    }


    public String createHost(String serviceName, String namespace, String clusterDomain) {
        return String.format("%s.%s.svc.%s", serviceName, StringUtils.hasText(namespace) ? namespace : "default",
                clusterDomain);
    }
}
