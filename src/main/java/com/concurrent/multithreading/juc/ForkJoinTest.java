package com.concurrent.multithreading.juc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 分支合并：fork/join
 *
 * @Date 2021/7/30
 * @Author MinJianPeng
 */
public class ForkJoinTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyTask myTask = new MyTask(1, 100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> submit = forkJoinPool.submit(myTask);
        System.out.println(submit.get());
        forkJoinPool.shutdown();

//        new ForkJoinTest().test(1, 100);


    }

    //测试拆分数据
    public void test(int begin, int end) {
        int middle = (begin + end) / 2;
        if (end - begin <= 10) {
            System.out.println(end + "---" + begin);
        } else {
            int begin1 = begin;
            int end1 = middle;
            if (end1 - begin1 <= 10) {
                System.out.println(end1 + "---" + begin1);
            } else {
                test(begin1, end1);
            }
            int begin2 = middle + 1;
            int end2 = end;
            if (end2 - begin2 <= 10) {
                System.out.println(end2 + "---" + begin2);
            } else {
                test(begin2, end2);
            }
        }
    }

}

/*
 *   十进制是逢十进一，二进制是逢二进一
 *   十进制转二进制方法：每次除以2，取余数1/0，最后倒序排列在一排
 *   二进制转十进制方法：取出每个数乘以2^n(n为该数字的位数，从0开始，2^0=1)，然后相加
 *   二进制向左移一位转十进制：十进制数除以2，取整，如果二进制最右边是1就加1，是0不用加
 *   二进制向右移一位转十进制：十进制数乘以2，如果二进制最右边是1就加1，是0不用加
 *
 *   77 = 1001101
 *   38  1  1 = 1*2^0
 *   19  0  0 = 0*2^1
 *   9   1  4 = 1*2^2
 *   4   1  8 = 1*2^3
 *   2   0  0 = 0*2^4
 *   1   0  0 = 0*2^5
 *   0   1  64= 0*2^6
 *
 *   1001111111 1 2 4 8 16 32 64   0   0 512 = 639
 *   1001000000 0 0 0 0  0  0 64   0   0 512 = 576
 *   100111111  1 2 4 8 16 32  0   0 256 = 319
 *   100100000  0 0 0 0  0 32  0   0 256 = 288
 *   10011111   1 2 4 8 16  0  0 128 = 159
 *   10010000   0 0 0 0 16  0  0 128 = 144
 *   1001111    1 2 4 8  0  0 64 = 79
 *   1001000    0 0 0 8  0  0 64 = 72
 *   100111     1 2 4 0  0 32 = 39
 *   100100     0 0 4 0  0 32 = 36
 *   10011      1 2 0 0 16 = 19
 *   10010      0 2 0 0 16 = 18
 *   1001       1 0 0 8 = 9
 */

class MyTask extends RecursiveTask<Integer> {
    public static final Integer VALUE = 10;
    private int begin;
    private int end;
    private int sum;

    public MyTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - begin <= VALUE) {
            for (int i = begin; i <= end; i++) {
                sum += i;
            }
        } else {
            int middle = (begin + end) / 2;
            MyTask task1 = new MyTask(begin, middle);
            MyTask task2 = new MyTask(middle + 1, end);
            task1.fork();
            task2.fork();
            sum = task1.join() + task2.join();
        }
        return sum;
    }
}