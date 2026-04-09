package tgb.cryptoexchange.variables.prize.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPrizeDTO {

    private String fiatCurrency;

    @Builder.Default
    private List<ReviewPrizeValueDTO> values = new ArrayList<>();

}
