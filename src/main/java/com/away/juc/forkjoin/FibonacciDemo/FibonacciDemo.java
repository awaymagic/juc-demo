package com.away.juc.forkjoin.FibonacciDemo;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 计算斐波那契数列
 * RecursiveTask 有返回值
 */
public class FibonacciDemo extends RecursiveTask<Integer> {

    final int n;

    FibonacciDemo(int n) {
        this.n = n;
    }

    /**
     * 重写 RecursiveTask的compute() 方法
     * @return 合并结果
     */
    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }
        FibonacciDemo f1 = new FibonacciDemo(n - 1);
        // 提交任务
        f1.fork();
        FibonacciDemo f2 = new FibonacciDemo(n - 2);
        // 合并结果(join 底层也是用的 ForkJoin)
        return f2.compute() + f1.join();
    }

    public static void main(String[] args) {
        // 构建forkjoin线程池
        ForkJoinPool pool = new ForkJoinPool();
        // 数很大容易栈溢出,用FibonacciDemo迭代进行优化
        FibonacciDemo task = new FibonacciDemo(100000);
        // 提交任务并一直阻塞直到任务 执行完成返回合并结果。
        int result = pool.invoke(task);
        System.out.println(result);
    }

}