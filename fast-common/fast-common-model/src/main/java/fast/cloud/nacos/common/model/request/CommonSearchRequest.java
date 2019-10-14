package fast.cloud.nacos.common.model.request;

import fast.cloud.nacos.common.model.condition.SearchCondition;
import lombok.Data;

import java.io.*;
import java.util.List;

@Data
public class CommonSearchRequest<T extends SearchCondition> implements Serializable {
    private List<String> fields;
    private T condition;
    private Sort sortBy;

    @Data
    public static class Sort implements Serializable {
        private String field;
        // 1: 正序; -1: 反序; 其他: 正序
        private Integer direction;

        @Override
        public String toString() {
            return "Sort{" +
                    "field='" + field + '\'' +
                    ", direction=" + direction +
                    '}';
        }
    }

    public static CommonSearchRequest deepClone(CommonSearchRequest src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(src);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Object o = ois.readObject();
        ois.close();
        return (CommonSearchRequest) o;
    }
}
