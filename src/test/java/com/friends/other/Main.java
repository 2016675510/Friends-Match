package com.friends.other;

public class Main {

//    public static void main(String[] args) {
//        // 通过匿名内部类方式访问接口
//        Formula formula = new Formula() {
//            @Override
//            public double calculate(int a) {
//                return sqrt(a * 100);
//
//            }
//        };
//
//        System.out.println(formula.calculate(100));     // 100.0
//        System.out.println(formula.sqrt(16));           // 4.0
//
//    }

}

class Formulator implements Formula{

    @Override
    public double calculate(int a) {
        return ceil(a);
    }

    @Override
    public double sqrt(int a) {
        return 0;
    }

    @Override
    public double ceil(int a) {
        return 0;
    }
}