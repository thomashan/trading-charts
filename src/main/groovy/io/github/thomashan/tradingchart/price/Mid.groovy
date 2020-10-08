package io.github.thomashan.tradingchart.price

class Mid implements Price {
    double value

    private Mid(double value) {
        this.value = value
    }

    static Mid of(double value) {
        return new Mid(value)
    }
}
