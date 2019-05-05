package codes.thischwa.bacoma.ui.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {
    @Value("${spring.application.name}")
    String appName;
 
    @GetMapping("/ui")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
}
