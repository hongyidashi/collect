package com.collect.algorithm.interview;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述: LRU算法
 *
 * @author: panhongtong
 * @date: 2021-07-24 07:27
 **/
public class LRUCache {

    private Node head;
    private Node end;
    private int limit;
    private Map<Integer,Node> map;

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(3);
        cache.put(1,"大福");
        cache.put(2,"断腿少女");
        cache.put(3,"大傻子");
        cache.put(4,"小智障");
        System.out.println("1:"+cache.get(1));
        System.out.println("2:"+cache.get(2));
        cache.put(5, "弟弟");
        System.out.println("3:"+cache.get(3)+"  2:"+cache.get(2));
    }

    public String get(Integer key) {
        Node node = map.get(key);
        if (node == null) {
            return null;
        }

        refreshNode(node);
        return node.value;
    }

    public void put(Integer key,String value) {
        Node node = map.get(key);
        if (node == null) {
            if (map.size() >= limit) {
                Integer oldKey = removeNode(head);
                map.remove(oldKey);
            }
            node = new Node(key, value);
            addNode(node);
            map.put(key, node);
        } else {
            node.value = value;
            refreshNode(node);
        }
    }

    public void remove(Integer key) {
        Node node = map.get(key);
        if (node == null) {
            return;
        }

        removeNode(node);
        map.remove(key);
    }

    /**
     * 刷新节点位置
     * @param node 带刷新位置的节点
     */
    public void refreshNode(Node node) {
        if (end == node) {
            return;
        }

        removeNode(node);
        addNode(node);
    }

    /**
     * 移除一个节点
     * @param node 待移除的节点
     * @return 被移除的节点的key
     */
    public Integer removeNode(Node node) {
        if (node == head && node == end) {
            // 唯一节点
            head = null;
            end = null;
        } else if (head == node) {
            // 头节点
            head = head.next;
            head.pre = null;
        } else if (end == node) {
            // 尾节点
            end = end.pre;
            end.next = null;
        } else {
            // 中间节点
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
        return node.key;
    }

    /**
     * 添加一个节点
     * @param node 待添加的节点
     */
    public void addNode(Node node) {
        if (end != null) {
            end.next = node;
            node.pre = end;
            node.next = null;
        }

        end = node;

        if (head == null) {
            head = node;
        }
    }

    public LRUCache(int size) {
        this.limit = size;
        map = new HashMap<>(size);
    }

    static class Node{
        private Node pre;
        private Node next;
        private Integer key;
        private String value;

        public Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

}
