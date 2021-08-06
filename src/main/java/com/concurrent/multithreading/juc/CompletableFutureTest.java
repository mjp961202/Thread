package com.concurrent.multithreading.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture
 * @Date 2021/7/30
 * @Author MinJianPeng
 */
public class CompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //同步调用
        CompletableFuture<Void> completableFuture=CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName());
        });
        System.out.println(completableFuture.get());

        //异步调用
        CompletableFuture<Integer> completableFuture1=CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName());
            return 111;
        });
        System.out.println(completableFuture1.whenComplete((t,u)->{
            System.out.println("t--"+t);
            System.out.println("u--"+u);
        }).get());
    }
}
