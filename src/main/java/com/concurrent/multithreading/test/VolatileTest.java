package com.concurrent.multithreading.test;

import lombok.extern.slf4j.Slf4j;

/**
 * volatile  可见性  操作共有属性
 *           有序性  防止指令重排
 */
@Slf4j
public class VolatileTest {
    private static volatile boolean flag=true;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            while (flag) {

            }
            log.debug("结束");
        });
        Thread t2 = new Thread(() -> {
            log.debug("开始");
            while (flag) {

            }
            log.debug("结束");
        });
        t1.start();
        t2.start();
        Thread.sleep(1000);
        log.debug("flag={}",flag);
        flag=false;
        log.debug("flag={}",flag);

    }
}
