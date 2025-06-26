package com.github.tanyuushaa.process;

import com.github.tanyuushaa.core.Product;
import com.github.tanyuushaa.db.ProductDb;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductDb productDb = new ProductDb();




    public void addProduct(Product product) {
        if (productDb.findByName(product.getName()) != null) {
            throw new IllegalArgumentException("Product already exists");
        }
        productDb.create(product);
    }


    public Product getProductById (int id) {
        return Optional.ofNullable(productDb.getById(id)).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public void updateProduct(Product product) {
        //Product existing = getProductById(product.getId());
        productDb.update(product);
    }

    public void deleteProductById(int id) {
        Product existing = getProductById(id);
        productDb.delete(id);
    }

    public List<Product> searchProduct(String name, String category, Integer minQty, Integer maxQty, Double minPr, Double maxPr) {
        return productDb.getAll(name, category, minQty, maxQty, minPr, maxPr);
    }

    public List<Product> getSortedProducts(String sortBy, boolean ascending) {
        List<Product> list = productDb.getAll(null, null, null, null, null, null);
        Comparator<Product> comparator = switch (sortBy.toLowerCase()) {
            case "price" ->Comparator.comparingDouble(Product::getPrice);
            case "quantity" -> Comparator.comparingInt(Product::getQuantity);
            case "category" -> Comparator.comparing(Product::getCategory, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
        };

        if (!ascending) comparator = comparator.reversed();
        list.sort(comparator);
        return list;
    }
}
