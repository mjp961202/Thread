package com.concurrent.multithreading.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁：synchronized隐式
 * Lock显示
 *
 * @Date 2021/7/28
 * @Author MinJianPeng
 */
public class SynLockTest {
    public static void main(String[] args) {
        SynLockTest s = new SynLockTest();
        s.add();
        s.syn();
        s.loc();
        s.del();
    }

    private int num = 0;

    public synchronized void add() {
        System.out.println(++num);
        if (num >= 10) {
            return;
        }
        add();
    }

    public void syn() {
        Object o = new Object();
        new Thread(() -> {
            synchronized (o) {
                System.out.println(Thread.currentThread().getName() + "外层");
                synchronized (o) {
                    System.out.println(Thread.currentThread().getName() + "内层");
                }
            }
        }, "t1").start();
    }

    Lock lock = new ReentrantLock();

    public void loc() {
        new Thread(() -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "外层2");
                try {
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + "内层2");
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    private int sum = 100;

    public void del() {
        try {
            lock.lock();
            System.out.println(--sum);
            if (sum <= 90) {
                return;
            }
            del();
        } finally {
            lock.unlock();
        }

    }
}
