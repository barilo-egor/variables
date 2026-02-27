package tgb.cryptoexchange.variables.bulkdiscount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tgb.cryptoexchange.variables.bulkdiscount.entity.OutboxEvent;

import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    Optional<OutboxEvent> findByAggregateId(Long aggregateId);

    List<OutboxEvent> findTop20ByProcessedFalseOrderByAggregateIdAsc();

}
