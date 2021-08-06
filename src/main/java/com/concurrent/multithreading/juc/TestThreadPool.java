package com.concurrent.multithreading.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 线程池 Executors----------不允许使用它，容易造成内存溢出OOM
 * newFixedThreadPool：固定数量线程池
 * newCachedThreadPool：缓存线程池，数量不固定，可以根据需求自动更改数量
 * newSingleThreadExecutor：创建单个线程池，池中只有一个线程
 * newScheduledThreadPool：固定数量线程池，可以延时或定时执行
 *
 * @Date 2021/7/21
 * @Author MinJianPeng
 */
public class TestThreadPool {
    public static void main(String[] args) throws Exception {
        TestThreadPool test = new TestThreadPool();
//        test.test1();
//        test.test2();
//        test.test3();
        test.test4();
    }

    public void test1() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        PoolDemo demo = new PoolDemo();
        for (int i = 0; i < 100; i++) {
            pool.submit(demo);
        }

        List<Integer> list = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            Future<Integer> submit = pool.submit(() -> {
                int sum = 0;
                for (int i = 0; i <= 100; i++) {
                    sum += i;
                }
                return sum;
            });
            list.add(submit.get());
        }
        list.forEach(System.out::println);
        pool.shutdown();
    }

    public void test2() throws Exception {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 10; i++) {
            ScheduledFuture<Integer> schedule = pool.schedule(() -> {
                int num = new Random().nextInt(100);
                System.out.println(Thread.currentThread().getName() + "：" + num);
                return num;
            }, 1, TimeUnit.SECONDS);
            System.out.println(schedule.get());
        }
        pool.shutdown();
    }

    public void test3() throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int j = 0; j < 10; j++) {
            Future<Integer> submit = pool.submit(() -> {
                int sum = 0;
                for (int i = 0; i <= 100; i++) {
                    System.out.println(Thread.currentThread().getName() + "：" + i);
                    sum += i;
                }
                return sum;
            });
            System.out.println(submit.get());
        }
        pool.shutdown();
    }

    public void test4() throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int j = 0; j < 10; j++) {
            pool.submit(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "：" + i);
                }
            });
        }
        pool.shutdown();
    }

}

class PoolDemo implements Runnable {
    private int i = 0;

    @Override
    public void run() {
        while (i < 100) {
            System.out.println(Thread.currentThread().getName() + "：" + i++);
        }
    }
}


