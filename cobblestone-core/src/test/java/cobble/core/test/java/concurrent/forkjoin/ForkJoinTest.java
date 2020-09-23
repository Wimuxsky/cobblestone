package cobble.core.test.java.concurrent.forkjoin;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {


    @Test
    public void test() {
        ForkJoinPool joinPool = ForkJoinPool.commonPool();
        RecursiveTask task = new RecursiveTask() {
            @Override
            protected Object compute() {
                return null;
            }
        };
        joinPool.submit(task);
        joinPool.invoke(task);
    }

}
