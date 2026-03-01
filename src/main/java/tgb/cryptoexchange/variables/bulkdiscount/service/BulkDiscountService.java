package tgb.cryptoexchange.variables.bulkdiscount.service;

import enums.CryptoCurrency;
import enums.DealType;
import enums.FiatCurrency;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.grpc.generated.BulkDiscountRequest;
import tgb.cryptoexchange.grpc.generated.BulkDiscountResponse;
import tgb.cryptoexchange.grpc.generated.BulkDiscountValueMessage;
import tgb.cryptoexchange.grpc.generated.UpdateBulkDiscountRequest;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscount;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscountValue;
import tgb.cryptoexchange.variables.bulkdiscount.repository.BulkDiscountRepository;
import tgb.cryptoexchange.variables.bulkdiscount.util.EventUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class BulkDiscountService {

    private final BulkDiscountRepository bulkDiscountRepository;

    private final BulkDiscountEventService bulkDiscountEventService;

    private final boolean isSendBulkDiscountEnabled;

    public BulkDiscountService(BulkDiscountRepository bulkDiscountRepository,
                               @Autowired(required = false) BulkDiscountEventService bulkDiscountEventService,
     @Value("${variables.bulk-discount.send-all-after-start:true}") boolean isSendBulkDiscountEnabled) {
        this.bulkDiscountRepository = bulkDiscountRepository;
        this.bulkDiscountEventService = bulkDiscountEventService;
        this.isSendBulkDiscountEnabled = isSendBulkDiscountEnabled;
    }

    @PostConstruct
    public void sendBulkDiscountEventAfterStart() {
        if (isSendBulkDiscountEnabled) {
            List<BulkDiscountResponse> bulkDiscounts = findAll();
            bulkDiscountEventService.process(EventUtil.mapToEvent(bulkDiscounts));
        }
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

    public List<BulkDiscountResponse> findAll() {
        log.debug("findAll bulkDiscount");
        return bulkDiscountRepository.findAll().stream().map(this::mapToResponse).toList();
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

        if (bulkDiscountEventService != null) {
            bulkDiscountEventService.process(EventUtil.mapToEvent(Collections.singletonList((mapToResponse(bulkDiscount)))));
        }
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
