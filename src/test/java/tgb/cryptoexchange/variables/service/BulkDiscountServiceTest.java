package tgb.cryptoexchange.variables.service;

import enums.CryptoCurrency;
import enums.DealType;
import enums.FiatCurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.cryptoexchange.grpc.generated.BulkDiscountRequest;
import tgb.cryptoexchange.grpc.generated.BulkDiscountResponse;
import tgb.cryptoexchange.grpc.generated.BulkDiscountValueMessage;
import tgb.cryptoexchange.grpc.generated.UpdateBulkDiscountRequest;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscount;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscountValue;
import tgb.cryptoexchange.variables.bulkdiscount.repository.BulkDiscountRepository;
import tgb.cryptoexchange.variables.bulkdiscount.service.BulkDiscountEventService;
import tgb.cryptoexchange.variables.bulkdiscount.service.BulkDiscountService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkDiscountServiceTest {

    @Mock
    private BulkDiscountRepository bulkDiscountRepository;

    private BulkDiscountService bulkDiscountService;

    @Mock
    private BulkDiscountEventService bulkDiscountEventService;

    private BulkDiscount testEntity;

    @BeforeEach
    void setUp() {
        bulkDiscountService = new BulkDiscountService(
                bulkDiscountRepository,
                bulkDiscountEventService,
                false
        );

        testEntity = new BulkDiscount();
        testEntity.setId(1L);
        testEntity.setFiatCurrency(FiatCurrency.RUB);
        testEntity.setDealType(DealType.BUY);
        testEntity.setCryptoCurrency(CryptoCurrency.BITCOIN);

        List<BulkDiscountValue> values = new ArrayList<>();
        values.add(BulkDiscountValue.builder().minAmount(new BigDecimal("1000")).discountRate(new BigDecimal("5")).build());
        values.add(BulkDiscountValue.builder().minAmount(new BigDecimal("500")).discountRate(new BigDecimal("2")).build());
        testEntity.setValue(values);
    }

    @Test
    void getBulkDiscount_Found_ReturnsMappedResponse() {
        BulkDiscountRequest request = BulkDiscountRequest.newBuilder()
                .setFiatCurrency("RUB").setDealType("BUY").setCryptoCurrency("BITCOIN").build();

        when(bulkDiscountRepository.findByFiatCurrencyAndDealTypeAndCryptoCurrency(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(testEntity));

        BulkDiscountResponse response = bulkDiscountService.getBulkDiscount(request);

        assertNotNull(response);
        assertEquals("RUB", response.getFiatCurrency());
        assertEquals(2, response.getValuesCount());
        assertEquals("1000", response.getValues(0).getMinAmount());
    }

    @Test
    void updateBulkDiscount_ExistingEntity_ClearsAndAddsNewValues() {
        UpdateBulkDiscountRequest request = UpdateBulkDiscountRequest.newBuilder()
                .setFiatCurrency("RUB").setDealType("BUY").setCryptoCurrency("BITCOIN")
                .addValues(BulkDiscountValueMessage.newBuilder().setMinAmount("2000").setDiscountRate("10").build())
                .build();

        when(bulkDiscountRepository.findByFiatCurrencyAndDealTypeAndCryptoCurrency(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(testEntity));

        bulkDiscountService.updateBulkDiscount(request);

        ArgumentCaptor<BulkDiscount> captor = ArgumentCaptor.forClass(BulkDiscount.class);
        verify(bulkDiscountRepository).save(captor.capture());

        BulkDiscount saved = captor.getValue();
        assertEquals(1, saved.getValue().size());
        assertEquals(new BigDecimal("10"), saved.getValue().getFirst().getDiscountRate());
    }

}
