package com.zh.conreoller;

import com.zh.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Administrator on 2017/12/7.
 */
@Controller
public class HomeConreoller {

    @GetMapping("/hello")
    public String test(Model model) {
        User user = new User(1001,"lisi","北京");
        model.addAttribute("user",user);
        return "hello";
    }
}
