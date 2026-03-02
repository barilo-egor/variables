package tgb.cryptoexchange.variables.repository;

import enums.CryptoCurrency;
import enums.DealType;
import enums.FiatCurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscount;
import tgb.cryptoexchange.variables.bulkdiscount.repository.BulkDiscountRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BulkDiscountRepositoryTest {

    @Autowired
    private BulkDiscountRepository bulkDiscountRepository;

    @BeforeEach
    void setUp() {
        bulkDiscountRepository.deleteAll();

        BulkDiscount discount = new BulkDiscount();
        discount.setFiatCurrency(FiatCurrency.RUB);
        discount.setDealType(DealType.BUY);
        discount.setCryptoCurrency(CryptoCurrency.BITCOIN);

        bulkDiscountRepository.save(discount);
    }

    @Test
    void findByParams_ShouldReturnEntity_WhenParamsMatch() {
        String fiat = "RUB";
        String deal = "BUY";
        String crypto = "BITCOIN";

        Optional<BulkDiscount> result = bulkDiscountRepository
                .findByFiatCurrencyAndDealTypeAndCryptoCurrency(fiat, deal, crypto);

        assertTrue(result.isPresent(), "Скидка должна быть найдена");
        assertEquals(FiatCurrency.RUB, result.get().getFiatCurrency());
        assertEquals(DealType.BUY, result.get().getDealType());
        assertEquals(CryptoCurrency.BITCOIN, result.get().getCryptoCurrency());
    }

    @Test
    void findByParams_ShouldReturnEmpty_WhenFiatNotMatches() {
        String fiat = "USD";
        String deal = "BUY";
        String crypto = "BITCOIN";

        Optional<BulkDiscount> result = bulkDiscountRepository
                .findByFiatCurrencyAndDealTypeAndCryptoCurrency(fiat, deal, crypto);

        assertTrue(result.isEmpty(), "Скидка не должна быть найдена при неверном FiatCurrency");
    }

    @Test
    void findByParams_ShouldReturnEmpty_WhenCryptoNotMatches() {
        String fiat = "RUB";
        String deal = "BUY";
        String crypto = "ETH";

        Optional<BulkDiscount> result = bulkDiscountRepository
                .findByFiatCurrencyAndDealTypeAndCryptoCurrency(fiat, deal, crypto);

        assertTrue(result.isEmpty(), "Скидка не должна быть найдена при неверном CryptoCurrency");
    }
}
