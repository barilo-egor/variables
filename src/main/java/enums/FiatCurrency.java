package enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.EnumTypeNotFoundException;
import interfaces.ObjectNodeConvertable;
import lombok.Generated;
import util.JacksonUtil;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

@JsonFormat(
        shape = JsonFormat.Shape.OBJECT
)
public enum FiatCurrency implements ObjectNodeConvertable<FiatCurrency> {
    BYN("byn", "Бел.рубли", "бел.рублей", "\ud83c\udde7\ud83c\uddfe", 2000, 30),
    RUB("rub", "Рос.рубли", "₽", "\ud83c\uddf7\ud83c\uddfa", 50000, 1000),
    UAH("uah", "Гривны", "гривен", "\ud83c\uddfa\ud83c\udde6", 10000, 10000);

    final String code;
    final String displayName;
    final String genitive;
    final String flag;
    final Integer defaultMaxSum;
    final Integer defaultMinSumForReferralDiscount;

    public String getName() {
        return this.name();
    }

    public static FiatCurrency getByCode(String code) {
        for (FiatCurrency fiatCurrency : values()) {
            if (fiatCurrency.getCode().equals(code)) {
                return fiatCurrency;
            }
        }

        throw new EnumTypeNotFoundException("Фиатная валюта не найдена.");
    }

    public Function<FiatCurrency, ObjectNode> mapFunction() {
        return (fiatCurrency) -> JacksonUtil.getEmpty().put("name", fiatCurrency.name()).put("code", fiatCurrency.getCode()).put("displayName", fiatCurrency.getDisplayName()).put("genitive", fiatCurrency.getGenitive()).put("flag", fiatCurrency.getFlag());
    }

    public static FiatCurrency valueOfNullable(String name) {
        if (Objects.isNull(name)) {
            return null;
        } else {
            FiatCurrency fiatCurrency;
            try {
                fiatCurrency = valueOf(name);
            } catch (IllegalArgumentException var3) {
                fiatCurrency = null;
            }

            return fiatCurrency;
        }
    }

    @Generated
    private FiatCurrency(final String code, final String displayName, final String genitive, final String flag, final Integer defaultMaxSum, final Integer defaultMinSumForReferralDiscount) {
        this.code = code;
        this.displayName = displayName;
        this.genitive = genitive;
        this.flag = flag;
        this.defaultMaxSum = defaultMaxSum;
        this.defaultMinSumForReferralDiscount = defaultMinSumForReferralDiscount;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public String getDisplayName() {
        return this.displayName;
    }

    @Generated
    public String getGenitive() {
        return this.genitive;
    }

    @Generated
    public String getFlag() {
        return this.flag;
    }

    @Generated
    public Integer getDefaultMaxSum() {
        return this.defaultMaxSum;
    }

    @Generated
    public Integer getDefaultMinSumForReferralDiscount() {
        return this.defaultMinSumForReferralDiscount;
    }

    public static class NameSerializer extends JsonSerializer<FiatCurrency> {
        public void serialize(FiatCurrency value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getName());
        }
    }

}
