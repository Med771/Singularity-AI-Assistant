package singularity.ai.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import singularity.ai.model.FAQ;
import singularity.ai.service.FAQService;

import java.util.List;

@RestController
@RequestMapping(path = "/append-faq")
public class FAQController {
    private final FAQService faqService;

    public FAQController(FAQService faqService) {
        this.faqService = faqService;
    }

    @GetMapping
    public ResponseEntity<List<FAQ>> findAll() {
        return ResponseEntity.ok(faqService.findAll());
    }

    @PostMapping
    public ResponseEntity<FAQ> appendFAQ(FAQ faq) {
        faqService.save(faq);

        return ResponseEntity.ok(faq);
    }


}