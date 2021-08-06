package com.concurrent.multithreading.test;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock  可重入锁
 */
@Slf4j
public class ReentrantLockTest {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        //test1();
        //test3();
        //test4();
        test5();
        //test6();
        //test7();
        //test8();
    }

    /**
     * 可重入锁  lock()
     * 释放锁    unlock()
     */
    public static void test1() {
        lock.lock();
        try {
            log.debug("111111");
            test2();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 可重入锁  lock()
     * 释放锁    unlock()
     */
    public static void test2() {
        lock.lock();
        try {
            log.debug("222222");
        } finally {
            lock.unlock();
        }
    }

    /**
     * 可打断锁  lockInterruptibly()
     * 打断      interrupt()
     */
    public static void test3() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                log.debug("尝试获取锁");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("被打断线程,没有获得锁");
                return;
            }
            try {
                log.debug("已获取到锁");
            } finally {
                log.debug("释放锁");
                lock.unlock();
            }
        });
        lock.lock();
        log.debug("启动线程");
        thread.start();
        Thread.sleep(1000);
        log.debug("打断线程");
        thread.interrupt();
        lock.unlock();
    }

    /**
     * 锁超时
     * tryLock()  获取到锁返回true
     * tryLock(long timeout, TimeUnit unit)  设置锁超时时间
     */
    public static void test4() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                log.debug("尝试获取锁");
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {
                    log.debug("没获取到");
                    return;
                }
                log.debug("获取到了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("释放锁");
            lock.unlock();
        });
        log.debug("主线程获取锁");
        lock.lock();
        thread.start();
        Thread.sleep(2500);
        log.debug("主线程释放锁");
        lock.unlock();
    }

    /**
     * newCondition()  创建休息室
     * await()  令线程进入休息室等待  放弃锁
     * signal()  唤醒一个在该休息室的线程  重新竞争锁
     * signalAll()  唤醒所有在该休息室的线程
     */
    public static void test5() throws InterruptedException {
        Condition con1 = lock.newCondition();
        Condition con2 = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("con1");
                con1.await();
                log.debug("con1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }).start();
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("con2");
                con2.await();
                log.debug("con2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }).start();
        Thread.sleep(2000);
        new Thread(() -> {
            lock.lock();
            try {
                con1.signal();
            } finally {
                lock.unlock();
            }

        }).start();
        Thread.sleep(2000);
        new Thread(() -> {
            lock.lock();
            try {
                con2.signalAll();
            } finally {
                lock.unlock();
            }
        }).start();
    }

    /**
     * 交替输出  wait  notify
     */
    public static void test6() {
        WaitNotify wn = new WaitNotify(1, 5);
        new Thread(() -> {
            wn.print("a", 1, 2);
        }).start();
        new Thread(() -> {
            wn.print("b", 2, 3);
        }).start();
        new Thread(() -> {
            wn.print("c", 3, 1);
        }).start();
    }

    /**
     * 交替输出  await  signal
     */
    public static void test7() throws InterruptedException {
        AwaitSignal as = new AwaitSignal(5);
        Condition c1 = as.newCondition();
        Condition c2 = as.newCondition();
        Condition c3 = as.newCondition();
        new Thread(() -> {
            as.print("a", c1, c2);
        }).start();
        new Thread(() -> {
            as.print("b", c2, c3);
        }).start();
        new Thread(() -> {
            as.print("c", c3, c1);
        }).start();
        Thread.sleep(2000);
        as.lock();
        try {
            c1.signal();
        } finally {
            as.unlock();
        }
    }

    static Thread c1;
    static Thread c2;
    static Thread c3;

    /**
     * 交替输出  pack  unpack
     */
    public static void test8() {
        PackUnpack pu = new PackUnpack(5);
        c1 = new Thread(() -> {
            pu.print("a", c2);
        });
        c2 = new Thread(() -> {
            pu.print("b", c3);
        });
        c3 = new Thread(() -> {
            pu.print("c", c1);
        });
        c1.start();
        c2.start();
        c3.start();
        LockSupport.unpark(c1);
    }

}

class WaitNotify {
    private int flag; //当前标记
    private int number; //循环次数

    public WaitNotify(int flag, int number) {
        this.flag = flag;
        this.number = number;
    }

    /**
     * 打印
     */
    public void print(String str, int waitFlag, int nextFlag) {
        for (int i = 0; i < number; i++) {
            synchronized (this) {
                while (flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}

class AwaitSignal extends ReentrantLock {
    private int number;

    public AwaitSignal(int number) {
        this.number = number;
    }

    public void print(String str, Condition con1, Condition con2) {
        for (int i = 0; i < number; i++) {
            lock();
            try {
                con1.await();
                System.out.print(str);
                con2.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}

class PackUnpack {
    private int number;

    public PackUnpack(int number) {
        this.number = number;
    }

    public void print(String str, Thread next) {
        for (int i = 0; i < number; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }
    }
}