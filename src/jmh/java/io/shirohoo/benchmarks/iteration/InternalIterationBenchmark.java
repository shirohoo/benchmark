package io.shirohoo.benchmarks.iteration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@Fork(3)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 500, timeUnit = TimeUnit.MILLISECONDS)
public class InternalIterationBenchmark {

    @Param({"100", "10000", "1000000"})
    private int n;

    private List<Long> getLongList() {
        List<Long> longList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            longList.add((long) i);
        }
        return longList;
    }

    @Benchmark
    public void internalConsume_1(Blackhole bh) {
        bh.consume(internalConsume_1(getLongList()));
    }

    public static long internalConsume_1(List<Long> longList) {
        return longList.stream()
            .reduce(0L, (i, j) -> i + i + j);
    }

    @Benchmark
    public void internalConsume_2(Blackhole bh) {
        bh.consume(internalConsume_2(getLongList()));
    }

    public static long internalConsume_2(List<Long> longList) {
        return LongStream.rangeClosed(1, longList.size())
            .sum();
    }

    @Benchmark
    public void internalGenerate_1(Blackhole bh) {
        bh.consume(internalGenerate_1(n));
    }

    public static long internalGenerate_1(long n) {
        return Stream.iterate(1L, i -> i + 1)
            .limit(n)
            .reduce(0L, (i, j) -> i + j);
    }

    @Benchmark
    public void internalGenerate_2(Blackhole bh) {
        bh.consume(internalGenerate_2(n));
    }

    public static long internalGenerate_2(long n) {
        return LongStream.rangeClosed(1, n)
            .sum();
    }

}