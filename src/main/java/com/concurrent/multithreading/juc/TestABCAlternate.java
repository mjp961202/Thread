package com.concurrent.multithreading.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 编写一个程序，开启3个线程，线程id分别为A、B、C，每个线程将打印10遍，要求输出结果按顺序显示ABCABC...
 *
 * @Date 2021/7/20
 * @Author MinJianPeng
 */
public class TestABCAlternate {
    public static void main(String[] args) {
        ABC abc = new ABC();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                abc.A();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                abc.B();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                abc.C();
            }
        }, "C").start();
    }
}

class ABC {
    private int num = 1;
    private Lock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void A() {
        try {
            lock.lock();
            if (num != 1) {
                conditionA.await();
            }
            System.out.print(Thread.currentThread().getName());
            num = 2;
            conditionB.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void B() {
        try {
            lock.lock();
            if (num != 2) {
                conditionB.await();
            }
            System.out.print(Thread.currentThread().getName());
            num = 3;
            conditionC.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void C() {
        try {
            lock.lock();
            if (num != 3) {
                conditionC.await();
            }
            System.out.print(Thread.currentThread().getName());
            num = 1;
            conditionA.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}