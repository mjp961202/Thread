package com.concurrent.multithreading.juc;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 闭锁，计数器为0才会执行后续代码
 * @Date 2021/7/29
 * @Author MinJianPeng
 */
public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(5);
        Down down=new Down(latch);
        for (int i = 1; i <= 5; i++) {
            new Thread(down,String.valueOf(i)).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"班长锁门！");
    }
}

class Down implements Runnable{

    private CountDownLatch latch;
    public Down(CountDownLatch latch){this.latch=latch;}

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+"号同学离开了教室！");
        }finally {
            latch.countDown();
        }
    }
}
