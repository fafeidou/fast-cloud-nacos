package fast.cloud.nacos.example.web.controller;


import fast.cloud.nacos.example.web.annotation.HttpApi;
import fast.cloud.nacos.example.web.annotation.HttpApiGroup;
import fast.cloud.nacos.example.web.bean.req.EnumTypeRequest;
import fast.cloud.nacos.example.web.enums.EnumContext;
import fast.cloud.nacos.example.web.enums.TypeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 公共获取枚举
 */

@RestController
@RequestMapping("/enumeration")
@HttpApiGroup("公共_枚举")
@Api(tags = "公共_枚举")
public class EnumerationController {

    @HttpApi("_默认_获取枚举信息列表")
    @GetMapping("/getEnumByName")
    @ApiOperation(value = "获取枚举信息列表", notes = "获取枚举信息列表")
    public List<TypeResponse> getEnumByName(
        @RequestParam @ApiParam(value = "枚举名称", required = true) String enumName) {
        return EnumContext.getEnumListByName(enumName);
    }

    @HttpApi("_默认_批量获取枚举信息列表")
    @PostMapping("/getEnumByNames")
    @ApiOperation(value = "批量获取枚举信息列表", notes = "批量获取枚举信息列表,返回值datas的key是传入的数组值，" +
        "value 是一个json：{'list':['code':'','value':'']}。格式同《获取枚举信息列表》接口返回值")
    public Map<String, List<TypeResponse>> getEnumByNames(
        @RequestBody @ApiParam(value = "枚举名称数组", required = true) EnumTypeRequest request) {
        return EnumContext.getEnumListByNames(request.getEnumNames());
    }

}
