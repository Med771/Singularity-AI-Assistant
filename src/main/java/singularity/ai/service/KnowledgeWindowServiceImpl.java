package singularity.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import singularity.ai.model.Answer;
import singularity.ai.model.Query;
import singularity.ai.model.UploadedFile;
import singularity.ai.repository.UploadedFileRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public final class KnowledgeWindowServiceImpl{
    // Init
    private final UploadedFileRepository uploadedFileRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.url}")
    private String apiUrl;

    // Constructor
    public KnowledgeWindowServiceImpl(UploadedFileRepository uploadedFileRepository) {
        this.uploadedFileRepository = uploadedFileRepository;
    }

    public Answer searchAnswerByQuery(Query query) {
        // AI process
        String res = this.getAnswer(query.getQuery());

        return new Answer(res);
    }

    public String getAnswer(String question) {
        // Получаем все файлы из базы данных
        List<UploadedFile> files = uploadedFileRepository.findAll();
        if (files.isEmpty()) {
            return "No relevant documents found.";
        }

        String knowledgeBase = this.getKnowledge();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", new Object[]{
                Map.of("role", "system", "content", "You are an assistant."),
                Map.of("role", "user", "content", "Knowledge base: " + knowledgeBase + "\nQuestion: " + question)
        });

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + openAiApiKey);
        headers.put("Content-Type", "application/json");

        return restTemplate.postForObject(apiUrl, Map.of("headers", headers, "body", requestBody), String.class);

    }

    public String getKnowledge() {
        // Получаем все файлы из базы данных
        List<UploadedFile> files = uploadedFileRepository.findAll();
        if (files.isEmpty()) {
            return "No relevant documents found.";
        }

        return files.stream()
                .map(file -> new String(file.getData())) // Преобразуем данные в строку (предполагаем, что это текст)
                .collect(Collectors.joining(" "));
    }
}

