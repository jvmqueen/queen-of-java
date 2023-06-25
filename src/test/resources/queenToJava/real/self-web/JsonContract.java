package com.selfxdsd.selfweb.api.output;

import com.selfxdsd.api.Contract;
import javax.json.Json;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class JsonContract extends AbstractJsonObject {

    private final String charset = "UTF-8";

    public JsonContract(final Contract contract) {
        this(contract, Boolean.FALSE);
    }

    public JsonContract(final Contract contract, final boolean withWalletType) {
        super(() -> {
            if (withWalletType) {
                return Json.createObjectBuilder().add("id", Json.createObjectBuilder().add("repoFullName", contract.contractId().getRepoFullName()).add("contributorUsername", contract.contractId().getContributorUsername()).add("provider", contract.contractId().getProvider()).add("role", contract.contractId().getRole()).build()).add("hourlyRate", NumberFormat.getCurrencyInstance(Locale.GERMANY).format(contract.hourlyRate().divide(BigDecimal.valueOf(100)))).add("value", NumberFormat.getCurrencyInstance(Locale.GERMANY).format(contract.value().divide(BigDecimal.valueOf(100)))).add("revenue", NumberFormat.getCurrencyInstance(Locale.GERMANY).format(contract.revenue().divide(BigDecimal.valueOf(100)))).add("markedForRemoval", String.valueOf(contract.markedForRemoval())).add("projectWalletType", contract.project().wallets().active().type()).build();
            } else {
                return Json.createObjectBuilder().add("id", Json.createObjectBuilder().add("repoFullName", contract.contractId().getRepoFullName()).add("contributorUsername", contract.contractId().getContributorUsername()).add("provider", contract.contractId().getProvider()).add("role", contract.contractId().getRole()).build()).add("hourlyRate", NumberFormat.getCurrencyInstance(Locale.GERMANY).format(contract.hourlyRate().divide(BigDecimal.valueOf(100)))).add("value", NumberFormat.getCurrencyInstance(Locale.GERMANY).format(contract.value().divide(BigDecimal.valueOf(100)))).add("revenue", NumberFormat.getCurrencyInstance(Locale.GERMANY).format(contract.revenue().divide(BigDecimal.valueOf(100)))).add("markedForRemoval", String.valueOf(contract.markedForRemoval())).build();
            }
        });
    }
}
