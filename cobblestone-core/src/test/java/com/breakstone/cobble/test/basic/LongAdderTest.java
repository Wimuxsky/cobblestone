package com.breakstone.cobble.test.basic;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class LongAdderTest {


    final static ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("Test Thread-%d")
            .setDaemon(true)
            .build();

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10, 20,
            10, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(10),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());


    @Test
    public void testPool() {
        Thread.currentThread().setName("TOP");
        IntStream.range(1, 100).forEach(i -> {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + ":" + i + " start");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + i + " finish");
            });
        });
        System.out.println("TOP finish");
    }


}
