package tgb.cryptoexchange.variables.bulkdiscount.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.variables.bulkdiscount.dto.BulkDiscountDTO;
import tgb.cryptoexchange.variables.bulkdiscount.kafka.BulkDiscountEvent;

import java.util.List;

@Service
@Slf4j
@Profile("!kafka-disabled")
public class BulkDiscountEventService {

    private final KafkaTemplate<String, List<BulkDiscountDTO>> kafkaTemplate;

    private final String bulkDiscountTopic;

    public BulkDiscountEventService(
            KafkaTemplate<String, List<BulkDiscountDTO>> kafkaTemplate,
            @Value("${kafka.topic.bulk-discount}") String bulkDiscountTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.bulkDiscountTopic = bulkDiscountTopic;
    }

    public void process(BulkDiscountEvent bulkDiscountEvent) {
        log.info("Найдено {} событий для отправки", bulkDiscountEvent.getValues().size());
        kafkaTemplate.send(bulkDiscountTopic, bulkDiscountEvent.getValues());
    }
}
