package tgb.cryptoexchange.variables.bulkdiscount.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.CryptoCurrency;
import enums.DealType;
import enums.FiatCurrency;
import exceptions.BodyMappingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import tgb.cryptoexchange.variables.bulkdiscount.dto.BulkDiscountValueDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkDiscountEvent {

    private FiatCurrency fiatCurrency;

    private DealType dealType;

    private CryptoCurrency cryptoCurrency;

    @Builder.Default
    private List<BulkDiscountValueDTO> value = new ArrayList<>();

    @Slf4j
    public static class KafkaSerializer implements Serializer<BulkDiscountEvent> {

        private static final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public byte[] serialize(String topic, BulkDiscountEvent discountEvent) {
            try {
                if (discountEvent == null) {
                    return new byte[0];
                }
                return objectMapper.writeValueAsBytes(discountEvent);
            } catch (JsonProcessingException e) {
                log.error("Ошибка сериализации объекта для отправки в топик {}: {}", topic, discountEvent);
                throw new BodyMappingException("Error occurred while mapping discountEvent", e);
            }
        }
    }
}
