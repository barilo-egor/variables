package tgb.cryptoexchange.variables.prize.review.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tgb.cryptoexchange.variables.exceptions.BodyMappingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import tgb.cryptoexchange.variables.prize.review.dto.ReviewPrizeDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPrizeEvent {

    @Builder.Default
    private List<ReviewPrizeDTO> values = new ArrayList<>();

    @Slf4j
    public static class KafkaSerializer implements Serializer<List<ReviewPrizeDTO>> {

        private static final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public byte[] serialize(String topic, List<ReviewPrizeDTO> reviewPrizeEvent) {
            try {
                if (reviewPrizeEvent == null) {
                    return new byte[0];
                }
                return objectMapper.writeValueAsBytes(reviewPrizeEvent);
            } catch (JsonProcessingException e) {
                log.error("Ошибка сериализации объекта для отправки в топик {}: {}", topic, reviewPrizeEvent);
                throw new BodyMappingException("Error occurred while mapping reviewPrizeEvent", e);
            }
        }
    }
}
