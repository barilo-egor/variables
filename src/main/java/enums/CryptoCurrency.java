package enums;

import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.EnumTypeNotFoundException;
import interfaces.ObjectNodeConvertable;
import util.JacksonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public enum CryptoCurrency implements ObjectNodeConvertable<CryptoCurrency> {
    BITCOIN("btc", 8, "Биткоин отправлен ✅\nhttps://blockstream.info/address/%s", "https://blockstream.info/tx/%s", "https://blockstream.info/address/%s", "Биткоин отправлен ✅"),
    LITECOIN("ltc", 8, "Валюта отправлена.\nhttps://live.blockcypher.com/ltc/address/%s", "https://live.blockcypher.com/ltc/tx/%s", "https://live.blockcypher.com/ltc/address/%s", "Валюта отправлена."),
    USDT("usdt", 6, "Валюта отправлена.https://tronscan.io/#/address/%s", "", "", "Валюта отправлена."),
    MONERO("xmr", 8, "Валюта отправлена.", "", "", "Валюта отправлена."),
    TRON("trx", 1, "Валюта отправлена", "", "", "Валюта отправлена."),
    ETHEREUM("eth", 5, "Валюта отправлена", "", "", "Валюта отправлена.");

    final String shortName;
    final int scale;
    final String sendMessage;
    final String hashUrl;
    final String addressUrl;
    final String message;
    public static final List<CryptoCurrency> CURRENCIES_WITH_AUTO_WITHDRAWAL = List.of(BITCOIN, LITECOIN);
    public static final List<CryptoCurrency> ELECTRUM_CURRENCIES = List.of(BITCOIN, LITECOIN);

    private CryptoCurrency(String shortName, int scale, String sendMessage, String hashUrl, String addressUrl, String message) {
        this.shortName = shortName;
        this.scale = scale;
        this.sendMessage = sendMessage;
        this.hashUrl = hashUrl;
        this.addressUrl = addressUrl;
        this.message = message;
    }

    public int getScale() {
        return this.scale;
    }

    public String getShortName() {
        return this.shortName;
    }

    public static CryptoCurrency fromShortName(String shortName) {
        return (CryptoCurrency) Arrays.stream(values()).filter((t) -> t.getShortName().equals(shortName)).findFirst().orElseThrow(() -> new EnumTypeNotFoundException("Не найдена крипто валюта: " + shortName));
    }

    public Function<CryptoCurrency, ObjectNode> mapFunction() {
        return (cryptoCurrency) -> JacksonUtil.getEmpty().put("name", cryptoCurrency.name()).put("shortName", cryptoCurrency.getShortName());
    }

    public static CryptoCurrency valueOfNullable(String name) {
        if (Objects.isNull(name)) {
            return null;
        } else {
            CryptoCurrency cryptoCurrency;
            try {
                cryptoCurrency = valueOf(name);
            } catch (IllegalArgumentException var3) {
                cryptoCurrency = null;
            }

            return cryptoCurrency;
        }
    }

    public String getSendMessage() {
        return this.sendMessage;
    }

    public String getHashUrl() {
        return this.hashUrl;
    }

    public String getMessage() {
        return this.message;
    }

    public String getAddressUrl() {
        return this.addressUrl;
    }
}
