package com.concurrent.multithreading.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class MethodTest {
    /**
     * 同步：需要等待结果返回才能继续运行的是同步
     * 异步：不要到等待结果返回就继续运行的是异步
     */
    public static void main(String[] args) throws InterruptedException {
        //MethodTest.test1();
        //MethodTest.test2();
        //MethodTest.test3();
        //MethodTest.test4();
        //MethodTest.test5();
        //MethodTest.test6();
        TwoPhaseTermination two=new TwoPhaseTermination();
        two.start();
        Thread.sleep(3010);
        two.stop();
    }

    /**
     * sleep  休眠  进入阻塞状态
     * interrupt  打断休眠  抛出异常
     * TimeUnit  时间工具类  也可以用于休眠
     */
    public static void test1() throws InterruptedException {
        Thread th = new Thread("test") {
            @Override
            public void run() {
                log.debug("test1");
                try {
                    Thread.sleep(3000);
                    log.debug("sleep");
                } catch (InterruptedException e) {
                    log.debug("error");
                    e.printStackTrace();
                }
            }
        };
        th.start();
        log.debug("main");
        TimeUnit.SECONDS.sleep(1);
        log.debug("main--sleep");
        th.interrupt();
    }

    /**
     * yield  谦让  进入就绪状态  只会调用就绪状态的线程
     * setPriority  设置优先级
     * cpu空闲时，几乎没有效果
     */
    public static void test2() {
        Runnable run1 = () -> {
            int count = 0;
            for (; ; ) {
                Thread.yield();
                System.out.println("111111----:" + count++);
            }
        };
        Runnable run2 = () -> {
            int count = 0;
            for (; ; ) {
                System.out.println("             222222----:" + count++);
            }
        };
        Thread thread1 = new Thread(run1, "t1");
        Thread thread2 = new Thread(run2, "t2");
        //thread1.setPriority(Thread.MIN_PRIORITY);
        //thread2.setPriority(Thread.MAX_PRIORITY);
        thread1.start();
        thread2.start();
    }

    static int n = 0;

    /**
     * join 占用线程
     */
    public static void test3() throws InterruptedException {
        log.debug("开始main");
        Thread thread = new Thread() {
            @Override
            public void run() {
                log.debug("开始");
                try {
                    sleep(1);
                    n = 10;
                    log.debug("结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        thread.join();
        log.debug("结果为：" + n);
        log.debug("结束main");
    }

    /**
     * interrupt 打断《休眠》线程，直接打断并抛出异常，interrupted=false
     * interrupt 打断《正常》线程，不会直接打断，interrupted=true
     * interrupted 获取打断标记，会清除标记
     * currentThread 获取当前线程对象
     * isInterrupted 获取打断标记，不会清除标记
     */
    public static void test4() {
        Thread thread = new Thread(() -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                log.debug("被打断");
//                return;
//            }
            while (true) {
                log.debug("n" + n++);
                Thread thread1 = Thread.currentThread();
                if (thread1.isInterrupted()) {
                    log.debug("被打断");
                    break;
                }
            }
        });
        thread.start();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        log.debug("interrupt");
        thread.interrupt();
    }

    static final Object o = new Object();

    /**
     * wait  需要先获得对象锁才能使用
     * wait  线程进入等待状态  并让出锁
     * wait(time)  等待时间到了自动结束等待状态
     * notify  唤醒等待状态线程
     * notifyAll 唤醒所有等待状态线程
     */
    public static void test5() {
        new Thread(() -> {
            log.debug("开始：" + Thread.currentThread().getName());
            synchronized (o) {
                try {
                    log.debug("等待：wait");
                    o.wait();
                    log.debug("结束等待：wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("执行完毕：" + Thread.currentThread().getName());
            }
        }, "t1").start();

        new Thread(() -> {
            log.debug("开始：" + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (o) {
                log.debug("执行：notify");
                o.notify();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("执行完毕：" + Thread.currentThread().getName());
            }
        }, "t2").start();
    }

    /**
     * LockSupport.park()  阻塞线程
     * LockSupport.unpark(thread)  唤醒指定线程--精确唤醒
     * 不需要搭配锁一起使用，可以先唤醒，后阻塞，阻塞无效
     */
    public static void test6() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                log.debug("开始");
                Thread.sleep(1000);
                log.debug("阻塞");
                LockSupport.park();
                log.debug("已唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        Thread.sleep(3000);
        log.debug("唤醒");
        LockSupport.unpark(thread);
    }
}

/**
 * 两阶段终止模式
 */
@Slf4j
class TwoPhaseTermination {
    private Thread monitor;

    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread thread = Thread.currentThread();
                if (thread.isInterrupted()) {
                    log.debug("被打断了，料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.debug("在休眠时被打断");
                    thread.interrupt();
                }
            }
        });
        monitor.start();
    }

    public void stop() {
        monitor.interrupt();
    }
}