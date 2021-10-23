package io.shirohoo.benchmarks;

import java.math.BigDecimal;
import org.openjdk.jmh.annotations.Benchmark;

public class OddEven {

    @Benchmark
    public void isEvenNumberWithBit() {
        int count = 0;
        for (long i = 0; i < 100_000_000; i++) {
            isEvenNumberWithBit(count);
        }
    }

    private boolean isEvenNumberWithBit(final int number) {
        return (number & 1) == 0;
    }

    @Benchmark
    public void isEvenNumber() {
        int count = 0;
        for (long i = 0; i < 100_000_000; i++) {
            isEvenNumber(count);
        }
    }

    private boolean isEvenNumber(final int number) {
        return number % 2 == 0;
    }

}
