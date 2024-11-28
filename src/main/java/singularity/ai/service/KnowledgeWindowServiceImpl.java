package singularity.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import singularity.ai.model.Answer;
import singularity.ai.model.Query;
import singularity.ai.repository.FAQRepository;

@Service
public final class KnowledgeWindowServiceImpl{
    // Init
    private final AIServiceImpl serviceAi;

    @Value("${login}")
    private String correctLogin;

    @Value("${password}")
    private String correctPassword;

    // Constructor
    public KnowledgeWindowServiceImpl(AIServiceImpl serviceAi, FAQRepository faqRepository) {
        this.serviceAi = new AIServiceImpl(faqRepository);
    }

    public Answer searchAnswerByQuery(Query query) {
        // AI process
        String res = serviceAi.processUserInput(query.getQuery());

        return new Answer(res);
    }
}

