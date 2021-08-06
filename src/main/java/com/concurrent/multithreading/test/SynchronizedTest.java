package com.concurrent.multithreading.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class SynchronizedTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SynchronizedTest s=new SynchronizedTest();
        //s.test();
        s.test1();
    }

    /**
     * FutureTask  带返回值的线程
     */
    public void test() throws ExecutionException, InterruptedException {
        FutureTask task=new FutureTask(() -> {log.debug("task");Thread.sleep(1000);return "task";});
        new Thread(task,"task").start();
        System.out.println(task.get());
    }

    public void test1() throws InterruptedException {
        Room room=new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                room.increment();
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                room.decrement();
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug(String.valueOf(room.getCount()));
    }

}

/**
 * synchronized 同步关键字
 * 加在成员方法上锁住的是this对象
 * 加在静态方法上锁住的是类对象
 */
class Room{
    private int count=0;

    public void increment(){
        synchronized(this) {
            count++;
        }
    }

    public void decrement(){
        synchronized(this) {
            count--;
        }
    }

    public synchronized int getCount(){
        return count;
    }
}