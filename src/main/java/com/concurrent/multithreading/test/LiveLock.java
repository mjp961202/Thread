package com.concurrent.multithreading.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 活锁
 * 互相改变对方结束条件
 */
@Slf4j
public class LiveLock {
    private static int count = 10;

    public static void main(String[] args) {
        new Thread(() -> {
            while (count > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
                log.debug(String.valueOf(count));
            }
        }).start();
        new Thread(() -> {
            while (count < 20) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                log.debug(String.valueOf(count));
            }
        }).start();
    }
}
