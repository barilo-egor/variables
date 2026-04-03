package tgb.cryptoexchange.variables.reviewPrize.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.variables.reviewPrize.dto.ReviewPrizeDTO;
import tgb.cryptoexchange.variables.reviewPrize.kafka.ReviewPrizeEvent;

import java.util.List;

@Service
@Slf4j
@Profile("!kafka-disabled")
public class ReviewPrizeEventService {

    private final KafkaTemplate<String, List<ReviewPrizeDTO>> kafkaTemplate;

    private final String reviewPrizeTopic;

    public ReviewPrizeEventService(
            KafkaTemplate<String, List<ReviewPrizeDTO>> kafkaTemplate,
            @Value("${kafka.topic.review-prize}") String reviewPrizeTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.reviewPrizeTopic = reviewPrizeTopic;
    }

    public void process(ReviewPrizeEvent reviewPrizeEvent) {
        int countDiscount = reviewPrizeEvent.getValues().size();
        log.trace("Найдено {} событий для отправки", countDiscount);
        if (countDiscount > 0) {
            kafkaTemplate.send(reviewPrizeTopic, reviewPrizeEvent.getValues());
        }
    }
}
