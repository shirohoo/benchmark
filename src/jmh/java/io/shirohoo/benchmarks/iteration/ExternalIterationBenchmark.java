package io.shirohoo.benchmarks.iteration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
public class ExternalIterationBenchmark {

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
    public void externalConsume(Blackhole bh) {
        bh.consume(externalConsume(getLongList()));
    }

    public static long externalConsume(List<Long> longList) {
        long sum = 0;
        for (Long aLong : longList) {
            sum += aLong;
        }
        return sum;
    }

    @Benchmark
    public void externalGenerate(Blackhole bh) {
        bh.consume(externalGenerate(n));
    }

    public static long externalGenerate(long n) {
        long sum = 0;
        for (long i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

}