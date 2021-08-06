package com.concurrent.multithreading.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 死锁
 * 检测：
 *     命令行：jps查询全部线程
 *             jstack 线程id  查询线程详情
 *     控制台：jconsole
 *
 * tryLock()  尝试获得锁
 */
@Slf4j
public class DeadLock {
    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("c1");
        Chopstick c2 = new Chopstick("c2");
        Chopstick c3 = new Chopstick("c3");
        Chopstick c4 = new Chopstick("c4");
        Chopstick c5 = new Chopstick("c5");
        new Philosopher("赵云", c1, c2).start();
        new Philosopher("关羽", c2, c3).start();
        new Philosopher("黄忠", c3, c4).start();
        new Philosopher("马超", c4, c5).start();
        new Philosopher("张飞", c5, c1).start();
    }
}

@Slf4j
class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            /**死锁*/
//            synchronized (left) {
//                synchronized (right) {
//                    eat(super.getName(),left.name,right.name);
//                }
//            }

            /**解决死锁*/
            if(left.tryLock()){
                try {
                    if(right.tryLock()){
                        try {
                            eat(super.getName(),left.name,right.name);
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }

        }
    }

    private void eat(String name, String left, String right) {
        log.debug(name+ "获得左手筷子"+ left+"右手筷子{}", right);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Chopstick extends ReentrantLock{
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子='" + name + "'";
    }
}