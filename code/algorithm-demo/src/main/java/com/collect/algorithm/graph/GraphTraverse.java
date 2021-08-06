package com.collect.algorithm.graph;

import java.util.LinkedList;

/**
 * 描述: 图遍历
 *
 * @author: panhongtong
 * @date: 2021-08-05 07:33
 **/
public class GraphTraverse {

    /**
     * 深度优先遍历
     * @param graph 图
     * @param start 起点
     * @param visited 用于存储顶点是否遍历过的数组
     */
    public static void dfs(Graph graph, int start, boolean[] visited) {
        System.out.println(graph.vertices[start].data);
        visited[start] = true;
        for (int index : graph.adj[start]) {
            if (!visited[index]) {
                dfs(graph, index, visited);
            }
        }
    }

    /**
     * 广度优先遍历
     * @param graph 图
     * @param start 起点
     * @param visited 用于存储顶点是否遍历过的数组
     * @param queue 队列，用于重放
     */
    public static void bfs(Graph graph, int start, boolean[] visited, LinkedList<Integer> queue) {
        queue.offer(start);

        while (!queue.isEmpty()) {
            int front = queue.poll();
            if (visited[front]) {
                continue;
            }
            System.out.println(graph.vertices[front].data);
            visited[front] = true;
            for (Integer index : graph.adj[front]) {
                queue.offer(index);
            }
        }

    }

    public static void main(String[] args) {
        Graph graph = new Graph(6);
        graph.adj[0].add(1);
        graph.adj[0].add(2);
        graph.adj[0].add(3);
        graph.adj[1].add(0);
        graph.adj[1].add(3);
        graph.adj[1].add(4);
        graph.adj[2].add(0);
        graph.adj[3].add(0);
        graph.adj[3].add(1);
        graph.adj[3].add(4);
        graph.adj[3].add(5);
        graph.adj[4].add(1);
        graph.adj[4].add(3);
        graph.adj[4].add(5);
        graph.adj[5].add(3);
        graph.adj[5].add(4);
        System.out.println("图的深度优先遍历：");
        dfs(graph, 0, new boolean[graph.size]);
        System.out.println("图的广度优先遍历：");
        bfs(graph, 0, new boolean[graph.size], new LinkedList<Integer>());
    }

    static class Graph {

        public int size;
        public Graph.Vertex[] vertices;
        public LinkedList<Integer>[] adj;

        public Graph(int size) {
            this.size = size;
            vertices = new Graph.Vertex[size];
            adj = new LinkedList[size];
            for (int i = 0; i < size; i++) {
                vertices[i] = new Graph.Vertex(i);
                adj[i] = new LinkedList<>();
            }
        }

        /**
         * 图顶点
         */
        public static class Vertex {
            public int data;

            public Vertex(int data) {
                this.data = data;
            }
        }

    }

}
