package com.concurrent.multithreading.juc;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore：计数信号量，指定容量，n个线程竞争，只有指定数量线程可以执行，只有释放之后其他线程才能执行
 * @Date 2021/7/29
 * @Author MinJianPeng
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore=new Semaphore(3);
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"辆车抢到了车位！");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                    System.out.println("----------"+Thread.currentThread().getName()+"辆车离开了车位！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
            },"第"+i).start();
        }
    }
}
