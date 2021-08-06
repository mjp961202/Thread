package com.concurrent.multithreading.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用于解决多线程安全问题的方式：
 * synchronized：隐式锁
 * 1、同步方法
 * 2、同步代码块
 *
 * jdk1.5以后
 * Lock：显示锁，童谣都过lock()方法上锁，必须通过unlock()方法释放锁
 * 3、同步锁
 *
 * @Date 2021/7/20
 * @Author MinJianPeng
 */
public class TestLock {
    public static void main(String[] args) {
        Ticket ticket=new Ticket();
        new Thread(ticket,"1号窗口").start();
        new Thread(ticket,"2号窗口").start();
        new Thread(ticket,"3号窗口").start();
    }
}

class Ticket implements Runnable{
    private int tick=1000;
    private Lock lock=new ReentrantLock();
    @Override
    public void run() {
        while (tick>0){
            lock.lock();
            try {
                if(tick>0) {
                    System.out.println(Thread.currentThread().getName() + "完成售票，余票为：" + --tick);
                }
            }finally {
                lock.unlock();
            }
        }
    }
}

