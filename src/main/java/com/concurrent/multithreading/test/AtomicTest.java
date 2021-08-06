package com.concurrent.multithreading.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.*;

@Slf4j
public class AtomicTest {
    public static void main(String[] args) {
        test1();
    }

    /**
     * 原子性操作
     * AtomicInteger  原子整数类型
     * AtomicReference<Object>  原子引用类型
     * AtomicStampedReference<Object>  带版本号的原子引用类型
     * AtomicIntegerArray  原子整数类型数组
     * AtomicReferenceArray<Object>  原子引用类型数组
     */
    public static void test1() {
        System.out.println("----------------------AtomicInteger------------------------");
        AtomicInteger ai = new AtomicInteger(0);
        System.out.println("自增并获取：" + ai.incrementAndGet() + "----" + ai.get());
        System.out.println("自减并获取：" + ai.decrementAndGet() + "----" + ai.get());
        System.out.println("获取并自增：" + ai.getAndIncrement() + "----" + ai.get());
        System.out.println("获取并自减：" + ai.getAndDecrement() + "----" + ai.get());
        System.out.println("相加并获取：" + ai.addAndGet(5) + "----" + ai.get());
        System.out.println("获取并相加：" + ai.getAndAdd(5) + "----" + ai.get());
        System.out.println("获取并修改：" + ai.getAndSet(1) + "----" + ai.get());
        System.out.println("运算并获取：" + ai.updateAndGet(value -> value * 10) + "----" + ai.get());

        System.out.println("----------------------AtomicReference<Double>------------------------");
        AtomicReference<Double> ar = new AtomicReference<>(10.25);
        System.out.println(ar.get());
        System.out.println(ar.getAndSet(12.00));
        System.out.println(ar.get());
        System.out.println(ar.updateAndGet(b -> b / 2));
        System.out.println(ar.compareAndSet(ar.get(), 5.55));
        System.out.println(ar.get());

        System.out.println("----------------------AtomicStampedReference<String>------------------------");
        AtomicStampedReference<String> asr = new AtomicStampedReference<>("A", 0);
        System.out.println(asr.getReference());
        System.out.println(asr.getStamp());
        System.out.println(asr.compareAndSet(asr.getReference(), "B", asr.getStamp(), asr.getStamp() + 1));
        System.out.println(asr.getReference());
        System.out.println(asr.getStamp());
        System.out.println(asr.compareAndSet("C", "C", asr.getStamp(), asr.getStamp() + 1));
        System.out.println(asr.getReference());
        System.out.println(asr.getStamp());
    }

}
