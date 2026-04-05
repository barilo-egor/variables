package tgb.cryptoexchange.variables.reviewPrize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tgb.cryptoexchange.variables.reviewPrize.entity.ReviewPrize;

import java.util.Optional;

public interface ReviewPrizeRepository extends JpaRepository<ReviewPrize, Long>, JpaSpecificationExecutor<ReviewPrize> {

    @Query("SELECT b FROM ReviewPrize b WHERE b.fiatCurrency = CAST(:fiat AS string)")
    Optional<ReviewPrize> findByFiatCurrency(@Param("fiat") String fiat);

}
