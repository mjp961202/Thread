package com.concurrent.multithreading.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 闭锁：在完成某些运算时，只有其他所有线程的运算全部完成，当前运算才继续执行
 *
 * @Date 2021/7/20
 * @Author MinJianPeng
 */
public class TestCountDownLatch {
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(5);//初始化锁存器计数
        LatchDemo lf = new LatchDemo(latch);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            new Thread(lf).start();
        }
        try {
            latch.await();//阻断线程，等待锁存器计数为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start));
    }
}

class LatchDemo implements Runnable {
    private CountDownLatch latch;

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                for (int i = 0; i < 1000; i++) {
                    if (i % 2 == 0) {
                        System.out.println(Thread.currentThread()+"-----"+i);
                    }
                }
            } finally {
                latch.countDown();//递减锁存器的计数
            }
        }
    }
}
