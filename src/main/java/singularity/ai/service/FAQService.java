package singularity.ai.service;

import org.springframework.stereotype.Service;
import singularity.ai.model.FAQ;
import singularity.ai.repository.FAQRepository;

import java.util.List;

@Service
public class FAQService {
    private final FAQRepository repository;

    public FAQService(FAQRepository repository) {
        this.repository = repository;
    }

    // Get
    public List<FAQ> findAll() {
        return repository.findAll();
    }

    // Post
    public void save(FAQ faq) {
        repository.save(faq);
    }
}
