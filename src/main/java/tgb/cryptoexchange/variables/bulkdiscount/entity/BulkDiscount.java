package tgb.cryptoexchange.variables.bulkdiscount.entity;

import enums.CryptoCurrency;
import enums.DealType;
import enums.FiatCurrency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private FiatCurrency fiatCurrency;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private DealType dealType;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private CryptoCurrency cryptoCurrency;

    @Builder.Default
    @OneToMany(mappedBy = "bulkDiscount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("discountRate ASC")
    private List<BulkDiscountValue> value = new ArrayList<>();

}
