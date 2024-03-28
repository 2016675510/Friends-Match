package com.friends.other.productFactory;

import com.friends.other.CarProduct;
import com.friends.other.ProductFactory;
import com.friends.other.product.BENZProduct;
import com.friends.other.product.BYDProduct;

/**
 * 比亚迪工厂类
 */
public class BYDProductFactory implements ProductFactory {
    String  name;
    String product;

    public BYDProductFactory(String name, String product) {
        this.name = name;
        this.product = product;
    }

    @Override
    public CarProduct createProduct() {
        return new BYDProduct(name,product);
    }
}
