package io.cityos.cityosair.data.model.reading;

public enum Timeframe {
    LIVE(""),
    DAY("day"),
    WEEK("week"),
    MONTH("month");

    private String rawValue;

    Timeframe(String rawEnum) {
        this.rawValue = rawEnum;
    }

    public String getRawValue() {
        return rawValue;
    }
}
