package fast.boot.webflux.controller;

import fast.boot.webflux.bean.DemoResponse;
import fast.boot.webflux.domain.User;
import fast.boot.webflux.repository.UserRepository;
import fast.boot.webflux.util.CheckUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${server.port}")
    private String port;

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * 以数组形式一次性返回数据
     *
     * @return
     */
    @GetMapping("/")
    public Flux<User> getAll() {
        return repository.findAll();
    }

    /**
     * 以SSE形式多次返回数据
     *
     * @return
     */
    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetAll() {
        return repository.findAll();
    }

    /**
     * 新增数据
     *
     * @param user
     * @return
     */
    @PostMapping("/")
    public Mono<User> createUser(@Valid @RequestBody User user) {
        // spring data jpa 里面, 新增和修改都是save. 有id是修改, id为空是新增
        // 根据实际情况是否置空id
        user.setId(null);
        CheckUtil.checkName(user.getName());
        return this.repository.save(user);
    }

    /**
     * 根据id删除用户 存在的时候返回200, 不存在返回404
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(
            @PathVariable("id") String id) {
        // deletebyID 没有返回值, 不能判断数据是否存在
        // this.repository.deleteById(id)
        return this.repository.findById(id)
                // 当你要操作数据, 并返回一个Mono 这个时候使用flatMap
                // 如果不操作数据, 只是转换数据, 使用map
                .flatMap(user -> this.repository.delete(user).then(
                        Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 修改数据 存在的时候返回200 和修改后的数据, 不存在的时候返回404
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable("id") String id,
                                                 @Valid @RequestBody User user) {
        CheckUtil.checkName(user.getName());
        return this.repository.findById(id)
                // flatMap 操作数据
                .flatMap(u -> {
                    u.setAge(user.getAge());
                    u.setName(user.getName());
                    return this.repository.save(u);
                })
                // map: 转换数据
                .map(u -> new ResponseEntity<User>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据ID查找用户 存在返回用户信息, 不存在返回404
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findUserById(
            @PathVariable("id") String id) {
        System.out.println("port===>" + port);
        return this.repository.findById(id)
                .map(u -> new ResponseEntity<User>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据年龄查找用户
     *
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/age/{start}/{end}")
    public Flux<User> findByAge(@PathVariable("start") int start,
                                @PathVariable("end") int end) {
        return this.repository.findByAgeBetween(start, end);
    }

    /**
     * 根据年龄查找用户
     *
     * @param start
     * @param end
     * @return
     */
    @GetMapping(value = "/stream/age/{start}/{end}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindByAge(@PathVariable("start") int start,
                                      @PathVariable("end") int end) {
        return this.repository.findByAgeBetween(start, end);
    }

    /**
     * 得到20-30用户
     *
     * @return
     */
    @GetMapping("/old")
    public Flux<User> oldUser() {
        return this.repository.oldUser();
    }

    /**
     * 得到20-30用户
     *
     * @return
     */
    @GetMapping(value = "/stream/old", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamOldUser() {
        return this.repository.oldUser();
    }


    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    Mono<DemoResponse> hello(@PathVariable("name") String name) {
        DemoResponse demoResponse = new DemoResponse();
        demoResponse.setMemo("hi " + name + " ,i am from port:" + port);
        return Mono.just(demoResponse);
    }
}
