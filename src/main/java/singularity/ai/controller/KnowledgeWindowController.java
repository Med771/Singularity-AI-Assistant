package singularity.ai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/window/test")
public class KnowledgeWindowController {
    // Get
    @GetMapping
    public String window(Model model) {
        return "window";
    }
}
