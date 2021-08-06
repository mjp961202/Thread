package com.concurrent.multithreading.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 卖票
 * new ReentrantLock(true)：公平锁，效率相对低
 * new ReentrantLock()：非公平锁，线程有可能饿死
 * @Date 2021/7/28
 * @Author MinJianPeng
 */
public class SellingTicketsTest {
    public static void main(String[] args) {
        Tick tick = new Tick();
        new Thread(() ->tick.run(), "张三").start();
        new Thread(() ->tick.run(), "李四").start();
        new Thread(() -> tick.run(), "王五").start();
    }
}

class Tick {
    private ReentrantLock lock = new ReentrantLock(true);
    private int num = 30;

    public void run() {
        while (num>0) {
            lock.lock();
            try {
                if (num > 0) {
                    System.out.println(Thread.currentThread().getName() + "卖出1张票，剩余：" + --num);
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
