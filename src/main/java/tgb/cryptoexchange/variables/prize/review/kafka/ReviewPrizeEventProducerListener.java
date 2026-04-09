package tgb.cryptoexchange.variables.prize.review.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.variables.prize.review.dto.ReviewPrizeDTO;

import java.util.List;

@Service
@Slf4j
@Profile({"!kafka-disabled"})
public class ReviewPrizeEventProducerListener implements ProducerListener<String, List<ReviewPrizeDTO>> {

    @Override
    public void onSuccess(ProducerRecord<String, List<ReviewPrizeDTO>> producerRecord, RecordMetadata recordMetadata) {
        log.debug("Успешно отправлен ивент. Key={}, event={}.", producerRecord.key(), producerRecord.value());
    }

    @Override
    public void onError(ProducerRecord<String, List<ReviewPrizeDTO>> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("Ошибка при попытке отправить ивент в топик. Key={}, event={}: {}.",
                producerRecord.key(), producerRecord.value(), exception.getMessage(), exception);
    }
}
