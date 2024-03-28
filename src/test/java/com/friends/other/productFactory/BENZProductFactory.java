package com.friends.other.productFactory;

import com.friends.other.CarProduct;
import com.friends.other.ProductFactory;
import com.friends.other.product.BENZProduct;
/**
 * 奔驰工厂类
 */
public class BENZProductFactory  implements ProductFactory {
    String name;
    String product;

    public BENZProductFactory(String name, String product) {
        this.name = name;
        this.product = product;
    }
    @Override
    public CarProduct createProduct() {
        return new BENZProduct(name, product);
    }
}
