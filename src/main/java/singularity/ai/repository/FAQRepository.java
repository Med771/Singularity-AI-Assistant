package singularity.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.ai.model.FAQ;

public interface FAQRepository extends JpaRepository<FAQ, Integer> {
}