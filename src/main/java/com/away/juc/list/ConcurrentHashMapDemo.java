package com.away.juc.list;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wei.guo
 * @date 2023/4/3
 */
public class ConcurrentHashMapDemo {

    private static final ConcurrentHashMap<String, AtomicLong> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    /** 创建一个 CountDownLatch 对象用于统计线程控制 */
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(3);
    /** 模拟文本文件中的单词 */
    private static final String[] WORDS = {"we", "it", "is"};

    public static void main(String[] args) {
        Runnable task = () -> {
            for (int i = 0; i < 3; i++) {
                // 模拟从文本文件中读取到的单词
                String word = WORDS[new Random().nextInt(3)];
                // 尝试获取全局统计结果
                AtomicLong number = CONCURRENT_HASH_MAP.get(word);
                // System.out.println(Thread.currentThread().getName() + "读取到单词: " + word);
                // 在未获取到的情况下，进行初次统计结果设置
                if (number == null) {
                    // 在设置时发现如果不存在则初始化
                    AtomicLong newNumber = new AtomicLong(0);
                    number = CONCURRENT_HASH_MAP.putIfAbsent(word, newNumber);
                    if (number == null) {
                        number = newNumber;
                    }
                }
                // 在获取到的情况下，统计次数直接加1
                number.incrementAndGet();

                System.out.println(Thread.currentThread().getName() + ":" + word + " 出现 " + number + " 次");
            }
            COUNT_DOWN_LATCH.countDown();
        };

        new Thread(task, "线程1").start();
        new Thread(task, "线程2").start();
        new Thread(task, "线程3").start();

        try {
            COUNT_DOWN_LATCH.await();
            System.out.println(CONCURRENT_HASH_MAP);
        } catch (Exception ignored) {}
    }

}
