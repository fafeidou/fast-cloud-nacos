package fast.cloud.nacos.mybatis.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author qfx
 */
public enum GradeEnum {
    PRIMARY(1, "小学"),  SECONDORY(2, "中学"),  HIGH(3, "高中");

    GradeEnum(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }


    private final int code;

    @EnumValue//标记数据库存的值是code
    private final String descp;

}
