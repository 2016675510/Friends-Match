package com.friends.other;

interface Formula{

    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
    default double ceil(int a) {
        return Math.ceil(a);
    }
    static void sm() {
        System.out.println("interface提供的方式实现");
    }
}

