package ru.michael.coco.overseer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/overseer")
public class OverseerController {
    @GetMapping
    public String getOverseerMainPage() {
        return "overseer/main";
    }
}
