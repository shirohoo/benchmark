# 서문

이 문서는 [melix/jmh-gradle-plugin](https://github.com/melix/jmh-gradle-plugin) 의 `README.md`를 번역한 문서입니다.

이 플러그인을 사용할 때 다음과 같은 문제를 마주칠 수 있습니다.

- `0.6.0` 이전 버전의 플러그인을 사용 할 경우 디렉토리 구조를 문서에 나와있는 것 처럼 직접 설정해야 합니다. 단, `0.6.0` 이후 버전의 플러그인을 사용 할 경우 디렉토리 구조가 자동적으로 설정됩니다.
- `Preferences` > `Build, Execution, Deployment` > `Compiler` > `Annotation Processors` 에 `Enable annotation processing`가 체크되어 있지 않다면 체크하십시오.
- 벤치마크할 소스는 `src/jmh/java` 하위에 존재해야 합니다.
- 인텔리제이의 Gradle 탭에서 벤치마크 수행 되지 않을 경우 터미널에서 `./gradlew jmh` 혹은 `gradle jmh`를 입력하여 수행해보세요.
- 이 문서를 작성한 작성자는 `0.6.6` 버전의 플러그인을 사용했으며, 벤치마크 수행 시 결과는 `build/results/jmh/results.txt`에 기록됐습니다.


# JMH Gradle Plugin

이 플러그인은 [JMH micro-benchmarking framework](https://openjdk.java.net/projects/code-tools/jmh/) 를 `Gradle`과 통합합니다.

## 사용

```groovy
// build.gradle
plugins {
  id 'me.champeau.jmh' version '0.6.6'
}
```

|경고|0.6.0 이전 버전의 플러그인 id는 `me.champeau.gradle.jmh`를 사용했습니다.|
|---|---|

샘플 코드는 [여기](https://github.com/melix/jmh-gradle-plugin/tree/master/samples) 에 있습니다.

## 어떤 플러그인 버전을 사용할 것인가?

`플러그인 0.6+` 이상은 `Gradle 6.8+`을 필요로 합니다.

|Gradle|Plugin|
|---|---|
|7.0|0.5.3|
|5.5|0.5.0|
|5.1|0.4.8|
|4.9|0.4.7(지연 작업 API 이점)|
|4.8|0.4.5|
|4.7|0.4.5|
|4.6|0.4.5|
|4.5|0.4.5|
|4.4|0.4.5|
|4.3|0.4.5|
|4.2|0.4.4|
|4.1|0.4.4|

## 구성

플러그인을 사용하면 특정 구성 덕분에 기존 프로젝트에 쉽게 통합할 수 있습니다. 특히 벤치마크 소스 파일은 `src/jmh`디렉토리 에서 찾을 수 있습니다.

```text
src/jmh
     |- java       : java sources for benchmarks
     |- resources  : resources for benchmarks
```

이 플러그인은 타사 라이브러리에 의존해야 할 경우 필요한 `jmh`구성을 생성합니다 . 예를 들어 `commons-io`를 사용하려는 경우 다음과 같이 종속성을 추가할 수 있습니다.

```groovy
// build.gradle
dependencies {
    jmh 'commons-io:commons-io:2.4'
}
```

플러그인은 JMH 1.29를 사용합니다. `dependencies`블록의 버전을 변경하기만 하면 버전을 업그레이드할 수 있습니다 .

```groovy
// build.gradle
dependencies {
    jmh 'org.openjdk.jmh:jmh-core:0.9'
    jmh 'org.openjdk.jmh:jmh-generator-annprocess:0.9'
}
```

## Gradle 태스크

이 플러그인은 프로젝트에 다음과 같은 몇가지 Gradle 태스크를 추가합니다.

- `jmhClasses` : 저수준의 벤치마크 코드를 컴파일합니다.
- `jmhRunBytecodeGenerator` : 저수준의 벤치마크 코드에 대해 바이트코드 생성기를 실행하고 실제 벤치마크를 생성합니다.
- `jmhCompileGeneratedClasses` : 생성된 벤치마크를 컴파일합니다.
- `jmhJar` : JMH 런타임과 컴파일된 벤치마크 클래스를 포함하는 JMH jar를 빌드합니다.
- `jmh` : 벤치마크를 실행합니다.

`jmh` 태스크는 메인 태스크이며 다른 태스크에 의존하므로 일반적으로 이 태스크를 실행하는 것으로 충분합니다.

```shell
gradle jmh
```

## 구성 옵션

기본적으로 모든 벤치마크가 실행되고 결과는 `$buildDir/reports/jmh`에 생성됩니다.

그러나 `jmh`구성 블록 덕분에 다양한 옵션을 변경할 수 있습니다. 

`includes`를 제외한 모든 옵션은 JMH 기본 값으로 설정됩니다.

```groovy
// build.gradle
jmh {
   includes = ['some regular expression'] // 실행할 벤치마크에 대한 패턴(정규 표현식) 포함.
   excludes = ['some regular expression'] // 실행할 벤치마크에 대한 제외 패턴(정규 표현식).
   iterations = 10 // 측정을 반복 수행할 횟수.
   benchmarkMode = ['thrpt','ss'] // 벤치마크 모드. 사용 가능한 모드: [Throughputthrpt, AverageTimeavgt, SampleTimesample, SingleShotTimess, Allall]
   batchSize = 1 // 배치 크기: 작업당 벤치마크 메서드 호출 수. (일부 벤치마크 모드는 이 설정을 무시할 수 있음)
   fork = 2 // 단일 벤치마크를 포크할 횟수입니다. 포크를 완전히 비활성화하려면 0을 사용.
   failOnError = false // 벤치마크에서 복구할 수 없는 오류가 발생한 경우 JMH가 즉시 실패해야 합니까?
   forceGC = false // JMH가 반복 간에 GC를 강제해야 합니까?
   jvm = 'myjvm' // 분기할 때 사용할 사용자 정의 JVM.
   jvmArgs = ['Custom JVM args to use when forking.']
   jvmArgsAppend = ['Custom JVM args to use when forking (append these)']
   jvmArgsPrepend =[ 'Custom JVM args to use when forking (prepend these)']
   humanOutputFile = project.file("${project.buildDir}/reports/jmh/human.txt") // 사람이 읽을 수 있는 출력 파일.
   resultsFile = project.file("${project.buildDir}/reports/jmh/results.txt") // 결과 파일.
   operationsPerInvocation = 10 // 호출당 작업.
   benchmarkParameters =  [:] // 벤치마크 매개변수.
   profilers = [] // 프로파일러를 사용하여 추가 데이터를 수집합니다. 지원되는 프로파일러: [cl, comp, gc, stack, perf, perfnorm, perfasm, xperf, xperfasm, hs_cl, hs_comp, hs_gc, hs_rt, hs_thr, async]
   timeOnIteration = '1s' // 각 측정 반복에 소요되는 시간.
   resultFormat = 'CSV' // 결과 형식 유형(CSV, JSON, NONE, SCSV, TEXT 중 하나)
   synchronizeIterations = false // 반복을 동기화하시겠습니까?
   threads = 4 // 실행할 작업자 스레드 수.
   threadGroups = [2,3,4] // 비대칭 벤치마크에 대한 스레드 그룹 분포를 재정의.
   timeout = '1s' // 벤치마크 반복 수행 시 타임아웃.
   timeUnit = 'ms' // 출력할 시간 단위. 사용 가능한 시간 단위: [m, s, ms, us, ns].
   verbosity = 'NORMAL' // 자세한 정보 표시 모드. 사용 가능한 모드: [SILENT, NORMAL, EXTRA]
   warmup = '1s' // 각 워밍업 반복에 소요되는 시간. 
   warmupBatchSize = 10 // 워밍업 배치 크기: 작업당 벤치마크 메서드 호출 수. 
   warmupForks = 0 // 단일 벤치마크에 대해 몇 개의 웜업 포크를 만들지. 0은 워밍업 포크를 비활성화.
   warmupIterations = 1 // 수행할 워밍업 반복 횟수.
   warmupMode = 'INDI' // 선택한 벤치마크를 웜업할 워밍업 모드: [INDI, BULK, BULK_INDI].
   warmupBenchmarks = ['.*Warmup'] // 이미 선택한 것 외에 실행에 포함할 워밍업 벤치마크. JMH는 이러한 벤치마크를 측정하지 않고 워밍업에만 사용합니다.

   zip64 = true // 더 큰 아카이브에 ZIP64 형식을 사용
   jmhVersion = '1.29' // JMH 버전 지정
   includeTests = true // 테스트 소스를 포함하여 JMH jar를 생성할 수 있습니다. 즉, 벤치마크가 테스트 클래스에 따라 달라질 때 사용합니다.
   duplicateClassesStrategy = DuplicatesStrategy.FAIL // fat jar를 생성하는 동안(즉, jmhJar 태스크를 실행하는 동안) 중복 클래스가 발생할 때 적용할 전략
}
```

## JMH 옵션 매핑

다음 표에서는 JMH의 명령줄 옵션과 플러그인의 확장 속성 간의 매핑을 설명합니다.


| JMH 옵션                  | 의미
|--------------------------|-------------------
| -bm <mode>               | benchmarkMode
| -bs <int>                | batchSize
| -e <regexp+>             | exclude
| -f <int>                 | fork
| -foe <bool>              | failOnError
| -gc <bool>               | forceGC
| -i <int>                 | iterations
| -jvm <string>            | jvm
| -jvmArgs <string>        | jvmArgs
| -jvmArgsAppend <string>  | jvmArgsAppend
| -jvmArgsPrepend <string> | jvmArgsPrepend
| -o <filename>            | humanOutputFile
| -opi <int>               | operationsPerInvocation
| -p <param={v,}*>         | benchmarkParameters?
| -prof <profiler>         | profilers
| -r <time>                | timeOnIteration
| -rf <type>               | resultFormat
| -rff <filename>          | resultsFile
| -si <bool>               | synchronizeIterations
| -t <int>                 | threads
| -tg <int+>               | threadGroups
| -to <time>               | timeout
| -tu <TU>                 | timeUnit
| -v <mode>                | verbosity
| -w <time>                | warmup
| -wbs <int>               | warmupBatchSize
| -wf <int>                | warmupForks
| -wi <int>                | warmupIterations
| -wm <mode>               | warmupMode
| -wmb <regexp+>           | warmupBenchmarks

## 프로젝트 파일에 대한 종속성

`jmh` 플러그인을 사용하면 별도의 프로젝트를 만들지 않고도 기존 소스를 쉽게 테스트할 수 있습니다. 따라서 `src/main/java` 대신 `src/jmh/java`에 벤치마크 소스 파일을 넣어야 합니다. 

즉, 기본적으로 `jmh` 태스크는 `main`(production) 소스 세트에 따라 달라집니다.

## Shadow Plugin과 함께 JMH Gradle Plugin사용

선택적으로 [Shadow Plugin](https://github.com/johnrengelman/shadow/)을 사용하여 실제 JMH jar 생성을 수행할 수 있습니다. 

JMH jar용 Shadow Plugin 구성은 `jmhJar` 블록을 통해 수행됩니다. 예:

```groovy
// build.gradle
jmhJar {
    append('META-INF/spring.handlers')
    append('META-INF/spring.schemas')
    exclude 'LICENSE'
}
```

## 중복 종속성 및 클래스

이 플러그인은 `jmh`, `runtime` 및 `testRuntime` 구성의 일부로 정의된 모든 종속성을 `jmhJar` 태스크를 실행할 때 생성되는 fat jar에 단일 집합으로 병합합니다. 

이렇게 하면 생성된 jar에 중복되는 종속성이 생기지 않습니다.

또한 플러그인은 fat jar를 만드는 동안 `duplicateClassStrategy` 확장 속성을 통해 정의된 [DuplicatesStrategy](https://docs.gradle.org/current/javadoc/org/gradle/api/file/DuplicatesStrategy.html )를 모든 클래스에 적용합니다. 

기본적으로 이 속성은 `DuplicatesStrategy.FAIL`로 설정됩니다. 중복 클래스가 감지되면 태스크가 실패됩니다.

`jmh` 블록을 통해 `duplicateClassStrategy` 속성을 구성하여 이 동작을 변경할 수 있습니다

```groovy
// build.gradle
jmh {
  duplicateClassesStrategy = DuplicatesStrategy.WARN
}
```

그러나 기본값에 문제가 발생할 경우 프로젝트의 클래스 패스나 소스에 중복 클래스가 포함되어 있음을 의미하므로 fat jar가 생성될 때 사용할 클래스를 예측할 수 없습니다.

[Shadow Plugin](https://github.com/johnrengelman/shadow/) 기능을 사용하는 클래스 이외의 중복 파일을 처리하려면 [Shadow Plugin과 함께 JMH Gradle Plugin 사용](https://github.com/melix/jmh-gradle-plugin#using-jmh-gradle-plugin-with-shadow-plugin) 을 참조하십시오 .

## 알려진 문제

벤치마크가 Groovy로 작성된 경우 Gradle과 함께 제공된 것과 동일한 버전의 Groovy를 사용해야 합니다. 

이는 향후 수정될 Gradle Worker API의 한계입니다.
