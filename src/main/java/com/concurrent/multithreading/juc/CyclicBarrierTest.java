package com.concurrent.multithreading.juc;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier：循环阻塞，每次执行障碍数加一，达到目标数之后才会继续执行
 * @Date 2021/7/29
 * @Author MinJianPeng
 */
public class CyclicBarrierTest {
    private static final int NUM = 7;

    public static void main(String[] args) {
        CyclicBarrier cyc = new CyclicBarrier(NUM, () -> System.out.println("集齐7颗龙珠了，开始召唤神龙！"));

        for (int i = 1; i <= 7; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "星龙珠被收集到了！");
                    cyc.await();
                    System.out.println(Thread.currentThread().getName()+"星龙珠被释放了！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }
}


