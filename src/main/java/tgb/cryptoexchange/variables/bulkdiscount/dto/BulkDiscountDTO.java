package tgb.cryptoexchange.variables.bulkdiscount.dto;

import enums.CryptoCurrency;
import enums.DealType;
import enums.FiatCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkDiscountDTO {

    private String fiatCurrency;

    private String dealType;

    private String cryptoCurrency;

    @Builder.Default
    private List<BulkDiscountValueDTO> values = new ArrayList<>();

}
