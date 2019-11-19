package fast.cloud.nacos.consumer.service;

import fast.cloud.nacos.consumer.domain.User;
import fast.cloud.nacos.webflux.ApiServer;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ApiServer(value = "http://localhost:8080/user", microName = "webflux-server")
public interface IUserApi {

    String ROOT = "/user";

    @GetMapping(ROOT + "/")
    Flux<User> getAllUser();

    @GetMapping(ROOT + "/{id}")
    Mono<User> getUserById(@PathVariable("id") String id);

    @DeleteMapping(ROOT + "/{id}")
    Mono<Void> deleteUserById(@PathVariable("id") String id);

    @PostMapping(ROOT + "/")
    Mono<User> createUser(@RequestBody Mono<User> user);
}
