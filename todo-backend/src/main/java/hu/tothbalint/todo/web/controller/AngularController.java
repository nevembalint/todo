package hu.tothbalint.todo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8888",
        "http://localhost:8080"})
public class AngularController {
    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
}
