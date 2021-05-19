package com.collect.multithreading.runnable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-05-20 07:28
 **/
public class RunnableDemo {

    public static void main(String[] args) throws Exception {
        testBlockedSocketState();
    }

    public static void testBlockedSocketState() throws Exception {
        Thread serverThread = new Thread(() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(10086);
                while (true) {
                    // 阻塞的accept方法
                    Socket socket = serverSocket.accept();
                    // dosoming
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "socket线程"); // 线程的名字
        serverThread.start();

        // 确保run已经得到执行
        Thread.sleep(500);

        // 状态为RUNNABLE
        System.out.println(serverThread.getState() == Thread.State.RUNNABLE);

    }

    public static void testInBlockedIOState() throws InterruptedException {
        Scanner in = new Scanner(System.in);
        // 创建一个名为“输入输出”的线程t
        Thread t = new Thread(() -> {
            try {
                // 命令行中的阻塞读
                String input = in.nextLine();
                System.out.println(input);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        }, "输入输出"); // 线程的名字

        // 启动
        t.start();

        // 确保run已经得到执行
        Thread.sleep(100);

        // 状态为RUNNABLE
        System.out.println(t.getState() == Thread.State.RUNNABLE);
    }

}
