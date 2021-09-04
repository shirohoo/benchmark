package io.shirohoo.benchmarks;

import java.math.BigDecimal;
import org.openjdk.jmh.annotations.Benchmark;

/*
같은 횟수를 반복하는 두개의 중첩 for문이 있을 경우에, 외부 루프가 더 많이 반복되는 것과 내부 루프가 더 많이 반복되는 것 중 어떤 것이 더 빠를까?

Java8 기준 2번 방법이 유의미 하게 빨랐다.
Java11 기준 유의미한 차이는 없다.

Benchmark                         Mode  Cnt  Score   Error  Units
NestedForLoop.nested_for_loop_1  thrpt    3  0.072 ± 0.029  ops/s
NestedForLoop.nested_for_loop_2  thrpt    3  0.073 ± 0.038  ops/s

 */
public class NestedForLoop {

    /*
    Result "io.shirohoo.benchmarks.NestedForLoop.nested_for_loop_1":
      0.072 ±(99.9%) 0.029 ops/s [Average]
      (min, avg, max) = (0.070, 0.072, 0.073), stdev = 0.002
      CI (99.9%): [0.043, 0.100] (assumes normal distribution)
     */
    @Benchmark
    public void nested_for_loop_1() {
        long count = 0;
        for (long i = 0; i < 10_000_000; i++) {
            for (long j = 0; j < 100; j++) {
                BigDecimal big = new BigDecimal("1000");
                count++;
            }
        }
    }

    /*
    Result "io.shirohoo.benchmarks.NestedForLoop.nested_for_loop_2":
      0.073 ±(99.9%) 0.038 ops/s [Average]
      (min, avg, max) = (0.070, 0.073, 0.074), stdev = 0.002
      CI (99.9%): [0.034, 0.111] (assumes normal distribution)
     */
    @Benchmark
    public void nested_for_loop_2() {
        long count = 0;
        for (long i = 0; i < 100; i++) {
            for (long j = 0; j < 10_000_000; j++) {
                BigDecimal big = new BigDecimal("1000");
                count++;
            }
        }
    }

}
