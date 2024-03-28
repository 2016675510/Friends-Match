package com.friends.other.product;

import com.friends.other.CarProduct;
/**
 * 奔驰产品类
 */
public class BENZProduct implements CarProduct {
    String  name;
    String product;
    String  productShow;

    public void getProductShow() {
        System.out.println("CarProduct{" +
                "name='" + name + '\'' +
                ", product=" + product +
                '}');
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getProduct() {
        return this.product;
    }

    public BENZProduct(String name, String product) {
        this.name = name;
        this.product = product;
    }
}
