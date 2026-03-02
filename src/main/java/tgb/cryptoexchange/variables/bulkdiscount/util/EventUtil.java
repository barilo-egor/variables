package tgb.cryptoexchange.variables.bulkdiscount.util;

import org.springframework.util.CollectionUtils;
import tgb.cryptoexchange.grpc.generated.BulkDiscountResponse;
import tgb.cryptoexchange.variables.bulkdiscount.dto.BulkDiscountDTO;
import tgb.cryptoexchange.variables.bulkdiscount.dto.BulkDiscountValueDTO;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscount;
import tgb.cryptoexchange.variables.bulkdiscount.kafka.BulkDiscountEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventUtil {

    private EventUtil() {
    }

    public static BulkDiscountEvent mapToEvent(List<BulkDiscountResponse> responseList) {
        if(CollectionUtils.isEmpty(responseList)){
            return BulkDiscountEvent.builder().build();
        }
        List<BulkDiscountDTO> values = responseList.stream().map(response-> BulkDiscountDTO.builder()
                .fiatCurrency(response.getFiatCurrency())
                .dealType(response.getDealType())
                .cryptoCurrency(response.getCryptoCurrency())
                .values(response.getValuesList().stream()
                        .map(item ->
                                BulkDiscountValueDTO.builder()
                                        .minAmount(item.getMinAmount())
                                        .discountRate(item.getDiscountRate())
                                        .build())
                        .collect(Collectors.toList()))
                .build()).toList();
        return BulkDiscountEvent.builder().values(values)
                .build();
    }

}
