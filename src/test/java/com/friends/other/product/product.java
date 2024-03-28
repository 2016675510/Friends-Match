package com.friends.other.product;

import com.friends.other.CarProduct;

public class product implements CarProduct {
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

    public product(String name, String product) {
        this.name = name;
        this.product = product;
    }
}
