package com.friends.other.productFactory;

import com.friends.other.CarProduct;
import com.friends.other.ProductFactory;
import com.friends.other.product.BMWProduct;

/**
 * 宝马工厂类
 */
public class BWMProductFactory implements ProductFactory {
    String name;
    String product;

    public BWMProductFactory(String name, String product) {
        this.name = name;
        this.product = product;
    }
    @Override
    public CarProduct createProduct() {
        return new BMWProduct(name, product);
    }
}
