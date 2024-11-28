package singularity.ai.service;

import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.stereotype.Service;
import singularity.ai.model.FAQ;
import singularity.ai.repository.FAQRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AIServiceImpl {
    // Init
    private final CosineSimilarity cosSim = new CosineSimilarity();
    private final FAQRepository faqRepository;

    // Constructor
    public AIServiceImpl(FAQRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    //
    public String processUserInput(String userInput) {
        // Преобразуем введенный текст в нижний регистр и убираем лишние пробелы
        String userQuery = userInput.toLowerCase().trim();

        List<FAQ> queries = faqRepository.findAll();

        double highestSimilarity = 0.0;
        String bestMatchAnswer = "Извините, я не поняла ваш вопрос.";

        // Преобразуем пользовательский ввод в карту с частотами слов
        Map<CharSequence, Integer> userQueryVector = toFrequencyMap(userQuery);

        // Ищем наиболее похожий вопрос
        for (FAQ faq : queries) {
            Map<CharSequence, Integer> faqVector = toFrequencyMap(faq.getQuestion().toLowerCase());
            double similarity = cosSim.cosineSimilarity(userQueryVector, faqVector);

            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatchAnswer = faq.getAnswer();
            }
        }

        return bestMatchAnswer;
    }

    // Преобразование строки в частотную карту слов
    private Map<CharSequence, Integer> toFrequencyMap(String text) {
        return Stream.of(text.split("\\s+"))
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }
}