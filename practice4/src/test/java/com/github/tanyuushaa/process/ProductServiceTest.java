package com.github.tanyuushaa.process;

import com.github.tanyuushaa.core.Product;
import com.github.tanyuushaa.db.ProductDb;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    private ProductService productService;

    @BeforeAll
    static void clearDb() {
        ProductDb db = new ProductDb();
        db.deleteAll();
    }

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    void testCreateProduct() {
        Product product = new Product("banana", "fruit", 99, 32.99);
        productService.addProduct(product);
        Product fetched = productService.searchProduct("banana", null, null, null, null, null).get(0);
        assertEquals("banana", fetched.getName());
        assertEquals("fruit", fetched.getCategory());
    }

    @Test
    void testUpdateProduct() {
        Product p = new Product("banana", "fruit", 99, 32.99);
        productService.addProduct(p);

        Product product = productService.searchProduct("banana", null, null, null, null, null).get(0);
        product.setQuantity(200);
        productService.updateProduct(product);
        Product updt = productService.getProductById(product.getId());
        assertEquals(200, updt.getQuantity());
    }

    @Test
    void testDeleteProduct() {
        Product p = new Product("banana", "fruit", 99, 32.99);
        productService.addProduct(p);
        Product delete = productService.searchProduct("banana", null, null, null, null, null).get(0);
        productService.deleteProductById(delete.getId());
        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(delete.getId()));
    }

    @Test
    void testSearchProductByCategory() {
        productService.addProduct(new Product("milk", "dairy", 55, 43.90));
        List<Product> res = productService.searchProduct(null, "dairy", null, null, null, null);
        assertFalse(res.isEmpty());
        assertEquals("dairy", res.get(0).getCategory());
    }

}