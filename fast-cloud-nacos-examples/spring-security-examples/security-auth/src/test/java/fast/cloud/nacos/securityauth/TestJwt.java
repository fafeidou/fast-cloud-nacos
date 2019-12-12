package fast.cloud.nacos.securityauth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJwt {

    //keytool -genkeypair -alias batman -keyalg RSA -keypass batman -keystore batman.keystore -storepass batmanstore
    //创建jwt令牌
    @Test
    public void testCreateJwt() {
        //密钥库文件
        String keystore = "batman.keystore";
        //密钥库的密码
        String keystore_password = "batmanstore";

        //密钥库文件路径
        ClassPathResource classPathResource = new ClassPathResource(keystore);
        //密钥别名
        String alias = "batman";
        //密钥的访问密码
        String key_password = "batman";
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keystore_password.toCharArray());
        //密钥对（公钥和私钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());
        //获取私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //jwt令牌的内容
        Map<String, String> body = new HashMap<>();
        body.put("name", "batman");
        String bodyString = JSON.toJSONString(body);
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(aPrivate));
        //生成jwt令牌编码
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }

    //校验jwt令牌
    @Test
    public void testVerify() {
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhOgZOVs31REV0cMp1s0Mv9hewMkqylx3ZHxF5yAVbQDWNdzqfKrDAKWf15u5sJDeOqBrtVyb3qPWhSg2Wi8h/xtwDuuu8sowF73qwY4+QR5Wisjbn0TUljhT0X5bzha6fy59oNtq+UXwSJCUrMXVBSwYtokYTFO7w0rHPWSp6b7FwxxenXCz7D+vPd/VJMfYkcTZm4iNlcrrWuVEP27lOlnJ6fqoy+PUb4stFAYGymv5AAOxmpiLfYWEA9MgG3r3vs6+EcynuX50I0PvN0G55pUUvWsdXa2ofjRKyDMPgC2odhTLlVRl+5h/IzRlllpD3WEhfNf1boAq1NH7IsVfPQIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiYmF0bWFuIn0.UR7Ox2nhl34SqpHB5FtLy3_6qYNOqw5IvwECfPqhbAxE_iSSVGPTuQkyp6rPs10UJq1sjKCuDYjw85AQGTZuexaNW5vjnGfU9pyQbcosHxHt80GC0mkhL8QCcMm1QxjwmYxFtI3Pugf-QU59EYOuVrEll2W-sW2oViISrLw1so3ZJvsijJGgidXdJi7KRfTnnEa76DHtRINMB5A8H5Dj9mbvSG3rNw1BOpticRq72RUpxj794MHPp5M2rauCxwNx2XI8XCPR6WUaldCwY7iEINxn0fgiV2nzhp4M6UFdVeguEm1Uiyx41ymIVqMPUGc0GgO6aAq6oe2ps82t83j8Xw";
        //校验jwt令牌
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}

