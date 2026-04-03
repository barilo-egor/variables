package tgb.cryptoexchange.variables.reviewPrize.entity;

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
public class ReviewPrize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private FiatCurrency fiatCurrency;

    @Builder.Default
    @OneToMany(mappedBy = "reviewPrize", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sum ASC")
    private List<ReviewPrizeValue> value = new ArrayList<>();

}
