package com.away.juc.forkjoin.recursiveaction;

import com.away.juc.forkjoin.util.Utils;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class ArrayToSortMain {

    public static void main(String[] args) {
        // 生成测试数组  (用于归并排序)
        int[] arrayToSortByMergeSort = Utils.buildRandomIntArray(20000000);
        // 生成测试数组  (用于 forkJoin 排序)
        int[] arrayToSortByForkJoin = Arrays.copyOf(arrayToSortByMergeSort, arrayToSortByMergeSort.length);
        // 获取处理器数量
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("处理器数量: " + processors);

        MergeSort mergeSort = new MergeSort(arrayToSortByMergeSort, processors);
        long startTime = System.nanoTime();
        // 归并排序
        int[] ints = mergeSort.mergeSort();
        long duration = System.nanoTime() - startTime;
        System.out.println("单线程归并排序时间: " + (duration / (1000f * 1000f)) + "毫秒");

        // 利用 forkJoin 排序
        MergeSortTask mergeSortTask = new MergeSortTask(arrayToSortByForkJoin, processors);
        // 构建 forkJoin 线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool(processors);
        startTime = System.nanoTime();
        // 执行排序任务
        forkJoinPool.invoke(mergeSortTask);
        duration = System.nanoTime() - startTime;
        System.out.println("forkJoin 排序时间: " + (duration / (1000f * 1000f)) + "毫秒");

    }
}
