package springboot.jpa.study;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", " hello!");
        return "hello";     //화면 이름 ... hello.html과 같다. hello.html로 넘어간다.
    }
}