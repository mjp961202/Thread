package com.concurrent.multithreading.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 创建线程的四种方式：实现Callable接口----带返回值,并且可以抛出异常
 * 继承Thread类
 * 实现Runnable接口
 * 线程池
 * 执行Callable方式，需要FutureTask实现类的支持，用于接收运算结果。FutureTask是Future接口的实现类
 *
 * @Date 2021/7/20
 * @Author MinJianPeng
 */
public class TestCallable {
    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();
        FutureTask<Integer> result = new FutureTask<>(td);
        new Thread(result).start();
        try {
            System.out.println(result.get());//线程执行完才会获取结果，可用于闭锁
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class ThreadDemo implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= 100; i++) {
            System.out.println(i);
            sum += i;
        }
        return sum;
    }
}
