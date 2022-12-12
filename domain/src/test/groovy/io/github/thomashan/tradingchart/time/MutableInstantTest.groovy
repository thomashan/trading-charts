package io.github.thomashan.tradingchart.time

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.Instant
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.stream.IntStream

class MutableInstantTest {
    private int numberOfThread = 10
    private ExecutorService executorService

    @BeforeEach
    void setUp() {
        this.executorService = Executors.newFixedThreadPool(numberOfThread)
    }

    @Test
    void testFrom_MultiThreadsProduceDifferentResults() {
        Callable<MutableInstant> mutableInstantCreator = {
            Instant now = Instant.now()
            return MutableInstant.from(now)
        }
        assertDifferentResults(mutableInstantCreator)
    }

    @Test
    void testParse_MultiThreadsProduceDifferentResults() {
        Callable<MutableInstant> mutableInstantCreator = {
            Instant now = Instant.now()
            return MutableInstant.parse(now.toString())
        }
        assertDifferentResults(mutableInstantCreator)
    }

    private void assertDifferentResults(Callable<MutableInstant> mutableInstantCreator) {
        List<Future<MutableInstant>> futures = IntStream.range(0, numberOfThread)
            .parallel()
            .mapToObj(mutableInstant -> executorService.submit(mutableInstantCreator as Callable<MutableInstant>))
            .toList()

        while (true) {
            if (futures.stream().allMatch(Future.&isDone)) {
                break
            }
        }
        List<MutableInstant> mutableInstants = new ArrayList(futures.stream()
            .map(Future.&get)
            .toList())
        while (!mutableInstants.isEmpty()) {
            MutableInstant mutableInstant = mutableInstants.pop()
            List<MutableInstant> sameMutableInstant = mutableInstants.stream()
                .filter(mutableInstantInList -> mutableInstantInList === mutableInstant)
                .toList()
            assert 0 == sameMutableInstant.size()
        }
    }
}
