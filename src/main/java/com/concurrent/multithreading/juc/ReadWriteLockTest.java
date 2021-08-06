package com.concurrent.multithreading.juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock：读写锁
 * 一个资源可以被多个“读”线程访问，或者被一个“写”线程访问，但是不能同时存在“读写”线程，读写互斥，读读共享
 * <p>
 * 锁降级：将写锁降级为读锁：获取写锁→获取读锁→释放写锁→释放读锁
 *
 * @Date 2021/7/29
 * @Author MinJianPeng
 */
public class ReadWriteLockTest {
    public static void main(String[] args) {
        ReadWriteLockTest test = new ReadWriteLockTest();
        test.rw();
        test.test();
    }

    //锁降级
    public void test() {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
        writeLock.lock();
        System.out.println("写");
        readLock.lock();
        System.out.println("读");
        writeLock.unlock();
        readLock.unlock();
    }

    //读写
    public void rw() {
        MyCache myCache = new MyCache();
        for (int i = 1; i <= 5; i++) {
            final int num = i;
            new Thread(() -> {
                myCache.put(num + "", num + "");
            }, String.valueOf(i)).start();
        }
        for (int i = 1; i <= 5; i++) {
            final int num = i;
            new Thread(() -> {
                System.out.println(myCache.get(num + ""));
            }, String.valueOf(i)).start();
        }
    }

}

class MyCache {
    private volatile Map<String, Object> map = new HashMap<>();
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void put(String key, String value) {
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "--开始put：" + key);
            TimeUnit.MICROSECONDS.sleep(300);
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "-----结束put：" + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public Object get(String key) {
        readWriteLock.readLock().lock();
        Object o = null;
        try {
            System.out.println(Thread.currentThread().getName() + "开始get--：" + key);
            o = map.get(key);
            TimeUnit.MICROSECONDS.sleep(300);
            System.out.println(Thread.currentThread().getName() + "结束get-----：" + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
        return o;
    }
}
