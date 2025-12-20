package com.wannaverse.users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello(Optional<String> name) {
        return "Hello, %s!".formatted(name.orElse("World"));
    }

}
