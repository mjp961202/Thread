package com.concurrent.multithreading.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 *
 * @Date 2021/7/20
 * @Author MinJianPeng
 */
public class TestReadWriteLock {
    public static void main(String[] args) {
        RWDemo rwDemo = new RWDemo();
        new Thread(() -> rwDemo.set((int) (Math.random() * 101)), "Write").start();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> rwDemo.get(), "Read").start();
        }
    }
}

class RWDemo {
    private int num = 0;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void get() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：" + num);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void set(int num) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：" + num);
            this.num = num;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
