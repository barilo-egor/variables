package tgb.cryptoexchange.variables.prize.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPrizeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer sum;

    @Column(nullable = false)
    private Integer minPrize;

    @Column(nullable = false)
    private Integer maxPrize;

    @ManyToOne
    @JoinColumn(name = "review_prize_id")
    private ReviewPrize reviewPrize;

}
