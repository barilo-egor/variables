package tgb.cryptoexchange.variables.bulkdiscount.util;

import tgb.cryptoexchange.variables.bulkdiscount.dto.BulkDiscountValueDTO;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscount;
import tgb.cryptoexchange.variables.bulkdiscount.kafka.BulkDiscountEvent;

import java.util.stream.Collectors;

public class EventUtil {

    public static BulkDiscountEvent mapToEvent(BulkDiscount entity) {
        return BulkDiscountEvent.builder()
                .fiatCurrency(entity.getFiatCurrency())
                .dealType(entity.getDealType())
                .cryptoCurrency(entity.getCryptoCurrency())
                .value(entity.getValue().stream()
                        .map(item ->
                                BulkDiscountValueDTO.builder()
                                        .minAmount(String.valueOf(item.getMinAmount()))
                                        .discountRate(String.valueOf(item.getDiscountRate()))
                                        .build())
                        .collect(Collectors.toList()))
                .build();
    }

}
