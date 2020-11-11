package io.github.thomashan.tradingchart.domain.price

class Mid implements Price {
    double value

    private Mid(double value) {
        this.value = value
    }

    static Mid of(double value) {
        return new Mid(value)
    }

    @Override
    String toString() {
        return "${value}"
    }
}
