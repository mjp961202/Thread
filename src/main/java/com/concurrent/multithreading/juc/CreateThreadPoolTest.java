package com.concurrent.multithreading.juc;

import sun.plugin2.gluegen.runtime.CPU;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池：ThreadPoolExecutor
 * 拒绝策略：抛异常→AbortPolicy；
 *           调用者运行→CallerRunsPolicy；
 *           丢弃队列中等待最久的，把当前任务加入队列并尝试提交→DiscardOldestPolicy；
 *           丢弃无法处理的(新加入的)，不予处理也不抛异常→DiscardPolicy
 *
 * 最大线程如何定义：
 *           1、CPU 密集型：几核就是几，可以保持CPU的效率最高
 *           2、IO  密集型：大于你程序中十分耗IO的线程
 * 获取CPU核数：Runtime.getRuntime().availableProcessors()
 * @Date 2021/7/30
 * @Author MinJianPeng
 */
public class CreateThreadPoolTest {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());

        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 3,
                2, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        for (int i = 0; i < 10; i++) {
            final int j=i;
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "开始运行："+j);
            });
        }
        pool.shutdown();
    }
}
