package com.zollos.crypto.app;

import com.binance.api.client.constant.BinanceApiConstants;
import com.binance.api.client.domain.general.SymbolInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Symbol {

    public static final String[] USD = { "USDT", "USDT", "TUSD", "USDC", "USDS", "BUSD" };
    public static final String[] EUR = { "EUR" };
    public static final String[] GBP = { "GBP" };
    public static final String[] GYEN = { "GYEN" };

    private final String symbolName;
    private final String status;
    private final String baseAsset;
    private final String quoteAsset;

    public Symbol(SymbolInfo symbolInfo) {

        this.symbolName = symbolInfo.getSymbol();
        this.status = symbolInfo.getStatus().toString();
        this.baseAsset = symbolInfo.getBaseAsset();
        this.quoteAsset = symbolInfo.getQuoteAsset();
    }

    public String getSymbolName() {
        return symbolName;
    }

    public String getStatus() {
        return status;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
                .append("symbol", symbolName)
                .append("status", status)
                .append("baseAsset", baseAsset)
                .append("quoteAsset", quoteAsset)
                .toString();
    }
}
