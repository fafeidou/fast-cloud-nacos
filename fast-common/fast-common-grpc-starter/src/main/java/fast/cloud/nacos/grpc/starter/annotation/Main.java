package fast.cloud.nacos.grpc.starter.annotation;

import com.alibaba.fastjson.JSONObject;
import fast.cloud.nacos.grpc.starter.service.GrpcRequest;
import fast.cloud.nacos.grpc.starter.util.ProtobufUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qinfuxiang
 * @Date 2020/6/11 21:30
 */
public class Main {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();

        map.put("clazz", "fast.cloud.nacos.grpc.api.service.UserServiceByProtoStuff");
        map.put("method", "findAll");

        byte[] bytes = JSONObject.toJSONBytes(map);
        GrpcRequest deserialize = ProtobufUtils.deserialize(bytes, GrpcRequest.class);
        System.out.println();
    }
}
//123,34,109,101,116,104,111,100,34,58,34,102,105,110,100,65,108,108,34,44,34,99,108,97,122,122,34,58,34,102,97,115,116,46,99,108,111,117,100,46,110,97,99,111,115,46,103,114,112,99,46,97,112,105,46,115,101,114,118,105,99,101,46,85,115,101,114,83,101,114,118,105,99,101,66,121,80,114,111,116,111,83,116,117,102,102,34,125
//    123 34 99 108 97 122 122 34 58 34 102 97 115 116 46 99 108 111 117 100 46 110 97 99 111 115 46 103 114 112 99 46 97 112 105 46 115 101 114 118 105 99 101 46 85 115 101 114 83 101 114 118 105 99 101 66 121 80 114 111 11  6 111 83 116 117 102 102 34 44 34 109 101 116 104 111 100 34 58 34 102 105 110 100 65 108 108 34 125