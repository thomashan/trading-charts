package io.github.thomashan.tradingchart.bytewatcher;
/*
 * Copyright (C) 2015 Daniel Shaya and Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.management.HotSpotDiagnosticMXBean;

import javax.management.MBeanServer;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ByteWatcherRegressionTestHelper {
    private static String heapDump = "heap_dump";
    private static String suffix = ".hprof";
    private static Path heapDumpPath = Path.of(heapDump);
    private final ByteWatcherSingleThread bw;
    private final HotSpotDiagnosticMXBean mxBean;

    public ByteWatcherRegressionTestHelper(Thread thread) {
        this.bw = new ByteWatcherSingleThread(thread);
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            this.mxBean = ManagementFactory.newPlatformMXBeanProxy(
                    server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ByteWatcherRegressionTestHelper() {
        this(Thread.currentThread());
    }

    public ByteWatcherRegressionTestHelper warmUp(Runnable job, long iterations) {
        return warmUp(job, iterations, false);
    }

    public ByteWatcherRegressionTestHelper warmUp(Runnable job, long iterations, boolean takeHeapDump) {
        for (int i = 0; i < iterations; i++) {
            job.run();
        }

        if (takeHeapDump) {
            takeHeapDump();
        }

        return this;
    }

    public void testAllocationNotExceeded(Runnable job, long limit, long iterations) {
        testAllocationNotExceeded(job, limit, iterations, false);
    }

    public void testAllocationNotExceeded(Runnable job, long limit, long iterations, boolean takeHeapDump) {
        bw.reset();
        for (int i = 0; i < iterations; i++) {
            job.run();
        }

        long size = bw.calculateAllocations();
        if (takeHeapDump) {
            takeHeapDump();
        }
        assertTrue(size <= limit, String.format("exceeded limit: %d using: %d%n", limit, size));
    }

    private void takeHeapDump() {
        try {
            if (!Files.exists(heapDumpPath)) {
                Files.createDirectories(heapDumpPath);
            }
            // FIXME: generating the file name generates garbage
            String heapDumpFile = heapDump + File.separator + Instant.now() + suffix;
            mxBean.dumpHeap(heapDumpFile, true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
