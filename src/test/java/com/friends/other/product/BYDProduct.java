package com.friends.other.product;

import com.friends.other.CarProduct;

/**
 * 比亚迪产品类
 */
public class BYDProduct implements CarProduct {
    String  name;
    String product;
    @Override
    public String getName() {
        return this.name;
    }

    public void getProductShow() {
        System.out.println("CarProduct{" +
                "name='" + name + '\'' +
                ", product=" + product +
                '}');
    }

    @Override
    public String getProduct() {
        return this.product;
    }

    public BYDProduct(String name, String product) {
        this.name = name;
        this.product = product;
    }
}
