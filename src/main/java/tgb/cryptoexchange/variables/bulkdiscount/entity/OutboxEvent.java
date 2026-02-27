package tgb.cryptoexchange.variables.bulkdiscount.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tgb.cryptoexchange.variables.bulkdiscount.kafka.BulkDiscountEvent;

@Entity
@Table(name = "outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long aggregateId;

    @JdbcTypeCode(SqlTypes.JSON)
    private BulkDiscountEvent payload;

    @Builder.Default
    private boolean processed = false;

}
