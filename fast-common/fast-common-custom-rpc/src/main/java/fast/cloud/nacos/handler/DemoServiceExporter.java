package fast.cloud.nacos.handler;

import lombok.SneakyThrows;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DemoServiceExporter extends AbstractDemoServiceExporter implements HttpRequestHandler {
    private CustomRpcServer demoRpcServer;


    @SneakyThrows
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        demoRpcServer.handle(request, response);
        response.getOutputStream().flush();
    }

    @Override
    void exportService() throws Exception {
        demoRpcServer = getJsonRpcServer();
    }
}
