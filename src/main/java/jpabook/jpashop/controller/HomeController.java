package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j  //log 찍기
public class HomeController {

    @RequestMapping("/")
    public String Home() {
        log.info("home controller");
        return "home";  //home.html로 찾아간다.
    }
}
