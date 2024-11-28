package singularity.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import singularity.ai.model.Answer;
import singularity.ai.model.Query;
import singularity.ai.service.KnowledgeWindowServiceImpl;

@RestController
@RequestMapping(path = "/window")
public class QueryController {
    // Init
    private final KnowledgeWindowServiceImpl service;

    // Constructor
    public QueryController(KnowledgeWindowServiceImpl service) {
        this.service = service;
    }

    @PostMapping(path = "/query")
    public ResponseEntity<Answer> getAnswer(@RequestBody Query query) {
        return ResponseEntity.ok(service.searchAnswerByQuery(query));
    }
}