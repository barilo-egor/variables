package tgb.cryptoexchange.variables.bulkdiscount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import enums.CryptoCurrency;
import enums.DealType;
import enums.FiatCurrency;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.grpc.generated.*;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscount;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscountValue;
import tgb.cryptoexchange.variables.bulkdiscount.entity.OutboxEvent;
import tgb.cryptoexchange.variables.bulkdiscount.repository.BulkDiscountRepository;
import tgb.cryptoexchange.variables.bulkdiscount.repository.OutboxEventRepository;
import tgb.cryptoexchange.variables.bulkdiscount.util.EventUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BulkDiscountService {

    private final BulkDiscountRepository bulkDiscountRepository;
    private final OutboxEventRepository outboxEventRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BulkDiscountService(BulkDiscountRepository bulkDiscountRepository, OutboxEventRepository outboxEventRepository) {
        this.bulkDiscountRepository = bulkDiscountRepository;
        this.outboxEventRepository = outboxEventRepository;
    }

    public BulkDiscountResponse getBulkDiscount(BulkDiscountRequest request) {
        log.debug("getBulkDiscount by request: {}", request);
        return bulkDiscountRepository.findByFiatCurrencyAndDealTypeAndCryptoCurrency(
                        request.getFiatCurrency(),
                        request.getDealType(),
                        request.getCryptoCurrency())
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Transactional
    public void updateBulkDiscount(UpdateBulkDiscountRequest request) {
        log.debug("updateBulkDiscount by request: {}", request);
        BulkDiscount bulkDiscount = bulkDiscountRepository.findByFiatCurrencyAndDealTypeAndCryptoCurrency(
                request.getFiatCurrency(),
                request.getDealType(),
                request.getCryptoCurrency()).orElse(null);

        if (bulkDiscount == null) {
            bulkDiscount = new BulkDiscount();
            bulkDiscount.setFiatCurrency(FiatCurrency.valueOfNullable(request.getFiatCurrency()));
            bulkDiscount.setDealType(DealType.valueOfNullable(request.getDealType()));
            bulkDiscount.setCryptoCurrency(CryptoCurrency.valueOfNullable(request.getCryptoCurrency()));
        } else {
            bulkDiscount.getValue().clear();
        }
        final BulkDiscount finalBulkDiscount = bulkDiscount;
        request.getValuesList().forEach(v -> {
            BulkDiscountValue newValue = BulkDiscountValue.builder()
                    .discountRate(new BigDecimal(v.getDiscountRate()))
                    .minAmount(new BigDecimal(v.getMinAmount()))
                    .bulkDiscount(finalBulkDiscount)
                    .build();
            finalBulkDiscount.getValue().add(newValue);
        });
        bulkDiscountRepository.save(bulkDiscount);

        saveOutboxEvent(bulkDiscount);
    }

    private void saveOutboxEvent(BulkDiscount bulkDiscount) {
        Optional<OutboxEvent> outboxEventOptional = outboxEventRepository.findByAggregateId(bulkDiscount.getId());
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateId(bulkDiscount.getId())
                .payload(EventUtil.mapToEvent(bulkDiscount))
                .processed(false)
                .build();
        outboxEventOptional.ifPresent(event -> outboxEvent.setId(event.getId()));
        outboxEventRepository.save(outboxEvent);
    }

    @Transactional
    public void syncBulkDiscountAndOutboxEvent(){
        List<BulkDiscount> allDiscounts = bulkDiscountRepository.findAll();
        allDiscounts.forEach(this::saveOutboxEvent);
    }

    private BulkDiscountResponse mapToResponse(BulkDiscount entity) {
        BulkDiscountResponse.Builder builder = BulkDiscountResponse.newBuilder()
                .setId(entity.getId());

        if (entity.getFiatCurrency() != null) builder.setFiatCurrency(entity.getFiatCurrency().name());
        if (entity.getDealType() != null) builder.setDealType(entity.getDealType().name());
        if (entity.getCryptoCurrency() != null) builder.setCryptoCurrency(entity.getCryptoCurrency().name());

        entity.getValue().forEach(v -> builder.addValues(BulkDiscountValueMessage.newBuilder()
                .setMinAmount(String.valueOf(v.getMinAmount()))
                .setDiscountRate(String.valueOf(v.getDiscountRate()))
                .build()));

        return builder.build();
    }

}
