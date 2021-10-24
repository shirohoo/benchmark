package io.shirohoo.benchmarks;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class EvenNumberChecker {

    @Benchmark
    public void isEvenNumber(Blackhole bh) {
        for (int i = 0; i < 100_000_000; i++) {
            bh.consume(isEvenNumber(i));
        }
    }

    public static boolean isEvenNumber(final int number) {
        return number % 2 == 0;
    }

    @Benchmark
    public void isEvenNumberWithBitwiseOperation(Blackhole bh) {
        for (int i = 0; i < 100_000_000; i++) {
            bh.consume(isEvenNumberWithBitwiseOperation(i));
        }
    }

    public static boolean isEvenNumberWithBitwiseOperation(final int number) {
        return (number & 1) == 0;
    }

}
