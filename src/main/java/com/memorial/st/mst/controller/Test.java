package com.memorial.st.mst.controller;


import com.sun.source.tree.Tree;

import java.util.*;

class DvException extends Exception {
    public int left;
    public int right;

    DvException () {
        super();
    }

    DvException(String message) {
        super(message);
    }

    DvException(String message, int left, int right) {
        super(message);
        this.left = left;
        this.right = right;
    }
}

class Cal {
    int left, right;

    public void setOp(int left, int right) {
        this.left = left;
        this.right= right;
    }

    public void divide() throws DvException {
        if (right == 0) {
            throw new DvException("0 asd", this.left, this.right);
        }
        System.out.println(this.left/this.right);
    }
}

class JaewonNumber {
    public int a;

    public JaewonNumber(int a) {
        this.a = a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public boolean equals(JaewonNumber o) {
        return this.a == o.a;
    }

    public boolean jaewonEquals(Object o) {
        if (o.getClass() == MinhyukNumber.class) {
            MinhyukNumber mh = (MinhyukNumber) o;
            return this.a == mh.a;
        }
        return false;
    }
}

class MinhyukNumber {
    public int a;

    public MinhyukNumber(int a) {
        this.a = a;
    }

    public void setA(int a) {
        this.a = a;
    }
}

class JaewonList {
}

public class Test {

    public static void main(String[] args) {
        int a = 1;
        int[] arr = new int[10];
        List<Integer> list = new LinkedList<>();    // 입구랑 출구가 정해져있어야함
        // doubleLinkedList
        Queue<Integer> q = new LinkedList<>();      // FIFO
        Stack<Integer> st = new Stack<>();          // FILO
        // 트리
        // Graph


        List<Integer> arrList = new ArrayList<>();


    }
}
