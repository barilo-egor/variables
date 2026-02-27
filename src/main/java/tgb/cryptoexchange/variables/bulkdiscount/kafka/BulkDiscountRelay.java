package tgb.cryptoexchange.variables.bulkdiscount.kafka;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tgb.cryptoexchange.variables.bulkdiscount.entity.OutboxEvent;
import tgb.cryptoexchange.variables.bulkdiscount.repository.OutboxEventRepository;
import tgb.cryptoexchange.variables.bulkdiscount.service.BulkDiscountService;

import java.util.List;

@Slf4j
@Component
@Profile("!kafka-disabled")
public class BulkDiscountRelay {

    private final OutboxEventRepository outboxRepository;

    private final BulkDiscountService bulkDiscountService;

    private final KafkaTemplate<String, BulkDiscountEvent> kafkaTemplate;

    private final String bulkDiscountTopic;

    public BulkDiscountRelay(OutboxEventRepository outboxRepository,
                             BulkDiscountService bulkDiscountService,
                             KafkaTemplate<String, BulkDiscountEvent> kafkaTemplate,
                             @Value("${kafka.topic.bulk-discount}") String bulkDiscountTopic) {
        this.outboxRepository = outboxRepository;
        this.bulkDiscountService = bulkDiscountService;
        this.kafkaTemplate = kafkaTemplate;
        this.bulkDiscountTopic = bulkDiscountTopic;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutbox() {
        if (outboxRepository.count() == 0) {
            bulkDiscountService.syncBulkDiscountAndOutboxEvent();
            outboxRepository.flush();
        }
        List<OutboxEvent> events = outboxRepository.findTop20ByProcessedFalseOrderByAggregateIdAsc();
        if (events.isEmpty()) {
            return;
        }
        log.info("Найдено {} событий в Outbox для отправки", events.size());
        boolean stopProcessing = false;
        for (int i = 0; i < events.size() && !stopProcessing; i++) {
            OutboxEvent event = events.get(i);
            try {
                kafkaTemplate.send(bulkDiscountTopic, event.getPayload()).get();
                event.setProcessed(true);
                outboxRepository.saveAndFlush(event);
            } catch (InterruptedException e) {
                log.error("Поток прерван при отправке {}", event.getId());
                Thread.currentThread().interrupt();
                stopProcessing = true;
            } catch (Exception e) {
                log.error("Ошибка при отправке события {}: {}", event.getId(), e.getMessage());
                stopProcessing = true;
            }
        }
    }
}
