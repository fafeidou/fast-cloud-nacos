package fast.cloud.k8s;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastK8sClientApplicationTests {

    @Test
    public void getAllPodListTest() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        V1PodList podList = k8sClient.getAllPodList();
        for (V1Pod item : podList.getItems()) {
            System.out.println(item.getMetadata().getNamespace() + ":" + item.getMetadata().getName());
        }
    }

    @Test
    public void getAllServiceListTest() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        V1ServiceList allServiceList = k8sClient.getAllServiceList();
        for (V1Service item : allServiceList.getItems()) {
            System.out.println(item.getMetadata().getNamespace() + ":" + item.getMetadata().getName());
        }
    }


    @Test
    public void getNamespace() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        String namespace = k8sClient.getApiServerAllNamespaceResponse("namespace");
        System.out.println(namespace);
    }

    @Test
    public void getNode() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        String node = k8sClient.getApiServerAllNamespaceResponse("node");
        System.out.println(node);
    }

    @Test
    public void getService() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        String service = k8sClient.getApiServerAllNamespaceResponse("service");
        System.out.println(service);
    }

    @Test
    public void getEndPoints() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        String endPoints = k8sClient.getApiServerAllNamespaceResponse("endPoints");
        System.out.println(endPoints);
    }

    @Test
    public void getConfigMap() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        String configMap = k8sClient.getApiServerAllNamespaceResponse("configMap");
        System.out.println(configMap);
    }

    @Test
    public void getDeployment() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        String deployment = k8sClient.getApiServerAllNamespaceResponse("deployment");
        System.out.println(deployment);
    }


    @Test
    public void getIngress() throws IOException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        String ingress = k8sClient.getApiServerAllNamespaceResponse("ingress");
        System.out.println(ingress);
    }

    @Test
    public void getServiceByName() throws IOException, ApiException {
        String kubeConfigPath = "config.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        List<V1Service> v1Services = k8sClient.getServiceByName("name-service");
        V1Service v1Service = v1Services.get(0);

        String host = k8sClient.createHost(v1Service.getMetadata().getName(),
                v1Service.getMetadata().getNamespace(), "cluster.local");
        System.out.println(v1Services);
    }

    @Test
    public void getServiceByName2() throws IOException, ApiException {
        String kubeConfigPath = "config-prd.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        List<V1Service> v1Services = k8sClient.getServiceByName("");
        V1Service v1Service = v1Services.get(0);

        String host = k8sClient.createHost(v1Service.getMetadata().getName(),
                v1Service.getMetadata().getNamespace(), "cluster.local");
        System.out.println(v1Services);
    }


    @Test
    public void getNode2() throws IOException {
        String kubeConfigPath = "config-prd.yml";
        K8sClient k8sClient = new K8sClient(kubeConfigPath);

        String node = k8sClient.getApiServerAllNamespaceResponse("node");
        System.out.println(node);
    }

}
