package tgb.cryptoexchange.variables.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tgb.cryptoexchange.variables.bulkdiscount.dto.BulkDiscountDTO;
import tgb.cryptoexchange.variables.bulkdiscount.kafka.BulkDiscountEvent;
import tgb.cryptoexchange.variables.bulkdiscount.kafka.BulkDiscountEventProducerListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableAsync
@EnableScheduling
public class CommonConfig {

    @Bean
    @Profile("!kafka-disabled")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    @Profile("!kafka-disabled")
    public ProducerFactory<String, List<BulkDiscountDTO>> bulkDiscountEventProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BulkDiscountEvent.KafkaSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    @Profile("!kafka-disabled")
    public KafkaTemplate<String, List<BulkDiscountDTO>> bulkDiscountEventKafkaTemplate(
            BulkDiscountEventProducerListener bulkDiscountEventProducerListener,
            KafkaProperties kafkaProperties) {
        KafkaTemplate<String, List<BulkDiscountDTO>> kafkaTemplate = new KafkaTemplate<>(
                bulkDiscountEventProducerFactory(kafkaProperties));
        kafkaTemplate.setProducerListener(bulkDiscountEventProducerListener);
        return kafkaTemplate;
    }

}
