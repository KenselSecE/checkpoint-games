package com.checkpointgames.checkpoint_games;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "fragments/home";
    }

    @GetMapping("/about")
    public String about() {
        return "fragments/about";
    }

    @GetMapping("/info")
    public String info() {
        return "fragments/info";
    }
}