package com.concurrent.multithreading.juc;

import java.util.concurrent.*;

/**
 * 线程池
 * @Date 2021/7/29
 * @Author MinJianPeng
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        ThreadPoolTest tp=new ThreadPoolTest();
        //tp.fixed();
        //tp.single();
        //tp.cached();
        tp.scheduled();
    }

    //固定数量线程池
    public void fixed(){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            executorService.execute(()-> System.out.println(Thread.currentThread().getName()+"办理业务"));
        }
        executorService.shutdown();
    }

    //单线程池
    public void single(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executorService.execute(()-> System.out.println(Thread.currentThread().getName()+"办理业务"));
        }
        executorService.shutdown();
    }

    //缓存线程池，数量可变
    public void cached(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(()-> System.out.println(Thread.currentThread().getName()+"办理业务"));
        }
        executorService.shutdown();
    }

    //定时线程池
    public void scheduled(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int j=i;
            ScheduledFuture<Integer> schedule = executorService.schedule(() -> {
                System.out.println(Thread.currentThread().getName() + "办理业务");
                return j;
            }, 1, TimeUnit.SECONDS);
            try {
                System.out.println(schedule.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }
}