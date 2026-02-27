package tgb.cryptoexchange.variables.repository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import tgb.cryptoexchange.variables.bulkdiscount.entity.OutboxEvent;
import tgb.cryptoexchange.variables.bulkdiscount.repository.OutboxEventRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class OutboxEventRepositoryTest {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Должен найти событие по aggregateId")
    void findByAggregateId_ShouldReturnEvent() {
        OutboxEvent event = new OutboxEvent();
        event.setAggregateId(100L);
        event.setProcessed(false);
        entityManager.persistAndFlush(event);

        Optional<OutboxEvent> found = outboxEventRepository.findByAggregateId(100L);

        assertThat(found).isPresent();
        assertThat(found.get().getAggregateId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Должен вернуть только необработанные события, отсортированные по ID")
    void findTop20_ShouldReturnUnprocessedOrderedByidAsc() {
        createEvent(30L, true);
        createEvent(10L, false);
        createEvent(50L, false);
        createEvent(20L, false);

        entityManager.flush();

        List<OutboxEvent> results = outboxEventRepository.findTop20ByProcessedFalseOrderByAggregateIdAsc();

        assertThat(results).hasSize(3);
        assertThat(results.get(0).getAggregateId()).isEqualTo(10L);
        assertThat(results.get(1).getAggregateId()).isEqualTo(20L);
        assertThat(results.get(2).getAggregateId()).isEqualTo(50L);
        assertThat(results).noneMatch(OutboxEvent::isProcessed);
    }

    @Test
    @DisplayName("Должен вернуть не более 20 записей")
    void findTop20_ShouldLimitResults() {
        java.util.stream.IntStream.rangeClosed(1, 25).forEach(i -> {
            OutboxEvent event = new OutboxEvent();
            event.setAggregateId((long) i);
            event.setProcessed(false);
            entityManager.persist(event);
        });

        entityManager.flush();
        entityManager.clear();

        List<OutboxEvent> results = outboxEventRepository.findTop20ByProcessedFalseOrderByAggregateIdAsc();

        assertEquals(20, results.size());
        assertEquals(1L, results.getFirst().getAggregateId());
    }

    private void createEvent(Long aggregateId, boolean processed) {
        OutboxEvent event = new OutboxEvent();
        event.setAggregateId(aggregateId);
        event.setProcessed(processed);
        entityManager.persist(event);
    }
}
