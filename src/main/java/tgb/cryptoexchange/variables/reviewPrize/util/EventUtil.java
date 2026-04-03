package tgb.cryptoexchange.variables.reviewPrize.util;

import org.springframework.util.CollectionUtils;
import tgb.cryptoexchange.grpc.generated.ReviewPrizeResponse;
import tgb.cryptoexchange.variables.reviewPrize.dto.ReviewPrizeDTO;
import tgb.cryptoexchange.variables.reviewPrize.dto.ReviewPrizeValueDTO;
import tgb.cryptoexchange.variables.reviewPrize.kafka.ReviewPrizeEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EventUtil {

    private EventUtil() {
    }

    public static ReviewPrizeEvent mapToEvent(List<ReviewPrizeResponse> responseList) {
        if(CollectionUtils.isEmpty(responseList)){
            return ReviewPrizeEvent.builder().build();
        }
        List<ReviewPrizeDTO> values = responseList.stream().map(response-> ReviewPrizeDTO.builder()
                .fiatCurrency(response.getFiatCurrency())
                .values(response.getValuesList().stream()
                        .map(item ->
                                ReviewPrizeValueDTO.builder()
                                        .sum(item.getSum())
                                        .minPrize(item.getMinPrize())
                                        .maxPrize(item.getMaxPrize())
                                        .build())
                        .collect(Collectors.toList()))
                .build()).toList();
        return ReviewPrizeEvent.builder().values(values)
                .build();
    }

}
