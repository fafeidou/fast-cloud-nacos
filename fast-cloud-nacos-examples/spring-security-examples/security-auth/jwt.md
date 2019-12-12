# 初探jwt
Spring Security 提供对JWT的支持，我们使用Spring Security 提供的JwtHelper来创建JWT令牌，校验JWT令牌
等操作
## 1.1生成私钥和公钥
JWT令牌生成采用非对称加密算法
* 生成密钥证书
下边命令生成密钥证书，采用RSA 算法每个证书包含公钥和私钥
```$xslt
keytool -genkeypair -alias batman -keyalg RSA -keypass batman -keystore batman.keystore -storepass batmanstore
Keytool 是一个java提供的证书管理工具
-alias：密钥的别名
-keyalg：使用的hash算法
-keypass：密钥的访问密码
-keystore：密钥库文件名，xc.keystore保存了生成的证书
-storepass：密钥库的访问密码
```
* 查询证书信息：
```
keytool -list -keystore batman.keystore
```
* 删除证书
```$xslt
keytool -delete -alias batman -keystore batman.keystore
```
* 导出公钥

```$xslt
keytool -list -rfc --keystore batman.keystore | openssl x509 -inform pem -pubkey
```

需要注意的是这里使用的openssl导出的：）

win环境下 ：
安装 openssl：http://slproweb.com/products/Win32OpenSSL.html
安装资料目录下的Win64OpenSSL-1_1_0g.exe
配置openssl的path环境变量，OpenSSL-Win64\bin 执行命令

mac环境下：
参考blog：https://blog.csdn.net/qyee16/article/details/72799852

下面粘贴出来我的公钥：
```
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhOgZOVs31REV0cMp1s0M
v9hewMkqylx3ZHxF5yAVbQDWNdzqfKrDAKWf15u5sJDeOqBrtVyb3qPWhSg2Wi8h
/xtwDuuu8sowF73qwY4+QR5Wisjbn0TUljhT0X5bzha6fy59oNtq+UXwSJCUrMXV
BSwYtokYTFO7w0rHPWSp6b7FwxxenXCz7D+vPd/VJMfYkcTZm4iNlcrrWuVEP27l
OlnJ6fqoy+PUb4stFAYGymv5AAOxmpiLfYWEA9MgG3r3vs6+EcynuX50I0PvN0G5
5pUUvWsdXa2ofjRKyDMPgC2odhTLlVRl+5h/IzRlllpD3WEhfNf1boAq1NH7IsVf
PQIDAQAB
-----END PUBLIC KEY-----
```
注意用的时候要放到一行！！！

## 1.2生成jwt令牌

我建了一个security-auth工程，github地址：https://github.com/fafeidou/fast-cloud-nacos/tree/master/fast-cloud-nacos-examples/spring-security-examples/security-auth，用于测试jwt，首先测试生成jwt令牌
```
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
   
```
程序会输出：
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiYmF0bWFuIn0.UR7Ox2nhl34SqpHB5FtLy3_6qYNOqw5IvwECfPqhbAxE_iSSVGPTuQkyp6rPs10UJq1sjKCuDYjw85AQGTZuexaNW5vjnGfU9pyQbcosHxHt80GC0mkhL8QCcMm1QxjwmYxFtI3Pugf-QU59EYOuVrEll2W-sW2oViISrLw1so3ZJvsijJGgidXdJi7KRfTnnEa76DHtRINMB5A8H5Dj9mbvSG3rNw1BOpticRq72RUpxj794MHPp5M2rauCxwNx2XI8XCPR6WUaldCwY7iEINxn0fgiV2nzhp4M6UFdVeguEm1Uiyx41ymIVqMPUGc0GgO6aAq6oe2ps82t83j8Xw
```

## 1.3验证jwt令牌

```
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
```

```
{"name":"batman"}
```


