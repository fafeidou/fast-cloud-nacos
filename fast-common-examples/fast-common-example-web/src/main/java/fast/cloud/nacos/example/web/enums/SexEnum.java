package fast.cloud.nacos.example.web.enums;

import fast.cloud.nacos.example.web.enums.commons.IntegerEnumInterface;
import fast.cloud.nacos.example.web.enums.commons.RegisterEnum;
import lombok.Getter;

/**
 * 调拨理由
 */
@RegisterEnum(value = "SexEnum", desc = "性别枚举", usedLocation = {"性别枚举"})
public enum SexEnum implements IntegerEnumInterface {

    /**
     * 女
     */
    WOMAN(1, "女"),

    /**
     * 男
     */
    MAN(2, "男"),

    ;

    @Getter
    private Integer code;

    @Getter
    private String desc;

    SexEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
