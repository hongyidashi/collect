package com.collect.netty.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @description: Files 演示
 * @author: panhongtong
 * @date: 2022/4/24 09:30
 **/
public class FilesDemo {

    public static void main(String[] args) {
//        traverseDir();
//        deleteMultiLevelDir();
        copyMultiLevelDir();
    }

    /**
     * 拷贝多级目录
     */
    static void copyMultiLevelDir() {
        long start = System.currentTimeMillis();
        String source = "/Users/panhongtong/nacos";
        String target = "/Users/panhongtong/nacos-test";

        try (Stream<Path> walk = Files.walk(Paths.get(source))) {
            walk.forEach(path -> {
                try {
                    String targetName = path.toString().replace(source, target);
                    // 是目录
                    if (Files.isDirectory(path)) {
                        Files.createDirectory(Paths.get(targetName));
                    }
                    // 是普通文件
                    else if (Files.isRegularFile(path)) {
                        Files.copy(path, Paths.get(targetName));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    /**
     * 删除多级目录
     */
    static void deleteMultiLevelDir() {
        Path path = Paths.get("/Users/panhongtong/logs");
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return super.visitFile(file, attrs);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                        throws IOException {
                    Files.delete(dir);
                    return super.postVisitDirectory(dir, exc);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 遍历目录
     */
    static void traverseDir() {
        Path path = Paths.get("/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk");
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    System.out.println(dir);
                    dirCount.incrementAndGet();
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    System.out.println(file);
                    fileCount.incrementAndGet();
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(dirCount);
        System.out.println(fileCount);
    }
}
