package io.shirohoo.benchmarks;

import java.math.BigDecimal;
import org.openjdk.jmh.annotations.Benchmark;

/*
같은 횟수를 반복하는 두 for문이 있을 경우에, 외부 루프가 더 많이 반복되는 것과 내부 루프가 더 많이 반복되는 경우
어떤 경우가 더 빠를까?
 */
public class NestedForSyntax {

    @Benchmark
    public void nested_for_test_1() {
        long count = 0;
        for (long i = 0; i < 10_000_000; i++) {
            for (long j = 0; j < 100; j++) {
                BigDecimal big = new BigDecimal("1000");
                count++;
            }
        }
    }

    @Benchmark
    public void nested_for_test_2() {
        long count = 0;
        for (long i = 0; i < 100; i++) {
            for (long j = 0; j < 10_000_000; j++) {
                BigDecimal big = new BigDecimal("1000");
                count++;
            }
        }
    }

}
