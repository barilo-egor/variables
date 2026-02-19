package tgb.cryptoexchange.variables.bulkdiscount.repository;

import enums.CryptoCurrency;
import enums.DealType;
import enums.FiatCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tgb.cryptoexchange.variables.bulkdiscount.entity.BulkDiscount;

import java.util.Optional;

public interface BulkDiscountRepository extends JpaRepository<BulkDiscount, Long>, JpaSpecificationExecutor<BulkDiscount> {

    @Query("SELECT b FROM BulkDiscount b WHERE " +
            "b.fiatCurrency = CAST(:fiat AS string) AND " +
            "b.dealType = CAST(:deal AS string) AND " +
            "b.cryptoCurrency = CAST(:crypto AS string)")
    Optional<BulkDiscount> findByFiatCurrencyAndDealTypeAndCryptoCurrency(@Param("fiat") String fiat,
                                       @Param("deal") String deal,
                                       @Param("crypto") String crypto);
}
