package fast.cloud.nacos.consumer.service;

import fast.cloud.nacos.consumer.domain.User;
import fast.cloud.nacos.webflux.ApiServer;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ApiServer("http://localhost:8080/user")
public interface IUserApi {

    @GetMapping("/")
    Flux<User> getAllUser();

    @GetMapping("/{id}")
    Mono<User> getUserById(@PathVariable("id") String id);

    @DeleteMapping("/{id}")
    Mono<Void> deleteUserById(@PathVariable("id") String id);

    @PostMapping("/")
    Mono<User> createUser(@RequestBody Mono<User> user);
}
