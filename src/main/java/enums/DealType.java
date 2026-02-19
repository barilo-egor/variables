package enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.EnumTypeNotFoundException;
import interfaces.ObjectNodeConvertable;
import lombok.Generated;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public enum DealType implements ObjectNodeConvertable<DealType>, Serializable {

    BUY("покупка", "покупку", "покупки", "buy"),
    SELL("продажа", "продажу", "продажи", "sell");

    final String nominative;
    final String genitive;
    final String accusative;
    final String key;

    private DealType(String nominative, String genitive, String accusative, String key) {
        this.nominative = nominative;
        this.genitive = genitive;
        this.accusative = accusative;
        this.key = key;
    }

    public String getNominativeFirstLetterToUpper() {
        String firstLetter = this.nominative.substring(0, 1).toUpperCase();
        return firstLetter + this.nominative.substring(1);
    }

    public static boolean isBuy(DealType dealType) {
        return BUY.equals(dealType);
    }

    public static DealType findByKey(String key) {
        return (DealType) Arrays.stream(values()).filter((dealType) -> dealType.getKey().equals(key)).findFirst().orElseThrow(EnumTypeNotFoundException::new);
    }

    public Function<DealType, ObjectNode> mapFunction() {
        return (dealType) -> (new ObjectMapper()).createObjectNode().put("name", dealType.name()).put("nominative", dealType.getNominative()).put("nominativeFirstUpper", dealType.getNominativeFirstLetterToUpper());
    }

    public static DealType valueOfNullable(String name) {
        if (Objects.isNull(name)) {
            return null;
        } else {
            DealType dealType;
            try {
                dealType = valueOf(name);
            } catch (IllegalArgumentException var3) {
                dealType = null;
            }

            return dealType;
        }
    }

    @Generated
    public String getNominative() {
        return this.nominative;
    }

    @Generated
    public String getGenitive() {
        return this.genitive;
    }

    @Generated
    public String getAccusative() {
        return this.accusative;
    }

    @Generated
    public String getKey() {
        return this.key;
    }


}


