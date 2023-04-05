package com.away.juc.list;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wei.guo
 * @date 2023/4/3
 */
public class CopyOnWriteArrayListDemo {

    private static final CopyOnWriteArrayList<String> COPY_ON_WRITE_ARRAY_LIST = new CopyOnWriteArrayList<>();

    // 模拟初始化的黑名单数据
    static {
        COPY_ON_WRITE_ARRAY_LIST.add("ipAddr0");
        COPY_ON_WRITE_ARRAY_LIST.add("ipAddr1");
        COPY_ON_WRITE_ARRAY_LIST.add("ipAddr2");
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            // 模拟接入用时
            try {
                Thread.sleep(new Random().nextInt(5000));
            } catch (Exception ignored) {}

            String currentIP = "ipAddr" + new Random().nextInt(6);
            if (COPY_ON_WRITE_ARRAY_LIST.contains(currentIP)) {
                System.out.println(Thread.currentThread().getName() + " IP " + currentIP + "命中黑名单，拒绝接入处理");
                return;
            }
            System.out.println(Thread.currentThread().getName() + " IP " + currentIP + "接入处理...");
        };

        new Thread(task, "请求1").start();
        new Thread(task, "请求2").start();
        new Thread(task, "请求3").start();

        new Thread(() -> {
            // 模拟用时
            try {
                Thread.sleep(new Random().nextInt(2000));
            } catch (Exception e) {}

            String newBlackIP = "ipAddr3";
            COPY_ON_WRITE_ARRAY_LIST.add(newBlackIP);
            System.out.println(Thread.currentThread().getName() + " 添加了新的非法IP " + newBlackIP);
        }, "IP黑名单更新").start();

        Thread.sleep(1000000);
    }

}
