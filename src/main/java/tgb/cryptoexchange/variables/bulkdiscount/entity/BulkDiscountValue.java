package tgb.cryptoexchange.variables.bulkdiscount.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BulkDiscountValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal discountRate;

    @ManyToOne
    @JoinColumn(name = "bulk_discount_id")
    private BulkDiscount bulkDiscount;

}
