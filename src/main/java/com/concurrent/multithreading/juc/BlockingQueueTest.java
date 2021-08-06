package com.concurrent.multithreading.juc;

import javax.lang.model.element.NestingKind;
import java.net.SocketTimeoutException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列
 * @Date 2021/7/29
 * @Author MinJianPeng
 */
public class BlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueueTest t=new BlockingQueueTest();
        //t.test1();
        //t.test2();
        //t.test3();
        t.test4();
    }

    //异常组合：add remove element
    public void test1(){
        BlockingQueue<String> bq=new ArrayBlockingQueue<>(3);
        //System.out.println(bq.element()); //队列为空会发生异常
        System.out.println(bq.add("a")); //添加元素
        System.out.println(bq.add("b"));
        System.out.println(bq.add("c"));
        //System.out.println(bq.add("d")); //超过容量会发生异常
        //System.out.println(bq.element()); //检索但不删除此队列的头部
        System.out.println(bq.remove("a")); //从此队列中移除指定元素的单个实例（如果存在）
        System.out.println(bq.remove()); //检索并删除此队列的头部
        System.out.println(bq.remove());
        //System.out.println(bq.remove());//队列为空会发生异常
    }

    //正常组合：offer poll peek
    public void test2(){
        BlockingQueue<String> bq=new ArrayBlockingQueue<>(3);
        System.out.println(bq.peek());//如果此队列为空，则返回null
        System.out.println(bq.offer("a"));//成功true
        System.out.println(bq.offer("b"));
        System.out.println(bq.offer("c"));
        System.out.println(bq.offer("d"));//失败false
        System.out.println(bq.peek());//检索但不删除此队列的头部
        System.out.println(bq.poll());//检索并移除此队列的头部
        System.out.println(bq.poll());
        System.out.println(bq.poll());
        System.out.println(bq.poll());//如果此队列为空，则返回null
    }

    //阻塞组合：put take
    public void test3() throws InterruptedException {
        BlockingQueue<String> bq=new ArrayBlockingQueue<>(3);
        bq.put("a"); //将指定的元素插入此队列
        bq.put("b");
        bq.put("c");
        //bq.put("d"); //等待可用空间，阻塞
        System.out.println(bq.take()); //检索并删除此队列的头部
        System.out.println(bq.take());
        System.out.println(bq.take());
        //System.out.println(bq.take()); //等待获取元素，阻塞
    }

    //超时组合：offer poll
    public void test4() throws InterruptedException {
        BlockingQueue<String> bq=new ArrayBlockingQueue<>(3);
        System.out.println(bq.offer("a"));
        System.out.println(bq.offer("b"));
        System.out.println(bq.offer("c"));
        System.out.println(bq.offer("d",3, TimeUnit.SECONDS));//指定时间未插入队列则放弃
        System.out.println(bq.poll());
        System.out.println(bq.poll());
        System.out.println(bq.poll());
        System.out.println(bq.poll(3,TimeUnit.SECONDS));//指定时间未获取元素则放弃
    }
}
