package com.concurrent.multithreading.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程通信
 * @Date 2021/7/28
 * @Author MinJianPeng
 */
public class SignalTest {
    public static void main(String[] args) {
        Signal signal=new Signal();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                signal.incr();
            }
        },"a1").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                signal.decr();
            }
        },"b1").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                signal.incr();
            }
        },"a2").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                signal.decr();
            }
        },"b2").start();
    }
}

class Signal {
    private ReentrantLock lock=new ReentrantLock();
    private Condition incr=lock.newCondition();
    private Condition decr=lock.newCondition();
    private int num=0;

    public void incr(){
        lock.lock();
        try {
            while (num!=0){
                try {
                    System.out.println("等待：自增");
                    incr.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"自增，num为："+ ++num);
            System.out.println("唤醒：自减");
            decr.signal();
        } finally {
            lock.unlock();
        }
    }

    public void decr(){
        lock.lock();
        try {
            while (num!=1){
                try {
                    System.out.println("等待：自减");
                    decr.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"自减，num为："+ --num);
            System.out.println("唤醒：自增");
            incr.signal();
        } finally {
            lock.unlock();
        }
    }
}
