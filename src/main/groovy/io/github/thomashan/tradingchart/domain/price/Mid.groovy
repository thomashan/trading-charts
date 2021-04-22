package io.github.thomashan.tradingchart.domain.price

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Mid implements Price {
    double value

    private Mid(double value) {
        this.value = value
    }

    static empty() {
        return of(0)
    }

    static Mid of(double value) {
        return new Mid(value)
    }

    @Override
    String toString() {
        return "${value}"
    }
}
