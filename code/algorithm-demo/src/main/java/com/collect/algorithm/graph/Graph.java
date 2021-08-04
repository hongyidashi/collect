package com.collect.algorithm.graph;

import java.util.LinkedList;

/**
 * 描述: 图
 *
 * @author: panhongtong
 * @date: 2021-08-05 07:34
 **/
public class Graph {

    public int size;
    public Vertex[] vertices;
    public LinkedList<Integer>[] adj;

    public Graph(int size) {
        this.size = size;
        vertices = new Vertex[size];
        adj = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            vertices[i] = new Vertex(i);
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
