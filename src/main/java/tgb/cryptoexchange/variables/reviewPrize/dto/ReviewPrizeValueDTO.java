package tgb.cryptoexchange.variables.reviewPrize.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewPrizeValueDTO {

    private Integer sum;

    private Integer minPrize;

    private Integer maxPrize;

}
