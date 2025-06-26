import com.github.tanyuushaa.core.Product;
import com.github.tanyuushaa.db.DbUtils;
import com.github.tanyuushaa.db.ProductDb;
import com.github.tanyuushaa.process.ProductService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DbTest {
    public static void main(String[] args) {
//        try(Connection connection = DbUtils.connect()) {
//            System.out.println("Connected to database");
//        } catch (SQLException e) {
//            System.err.println(e.getMessage());
//        }
        //Product product = new Product("milk", "food", 100, 23.5);
        //System.out.println(product);

        //new ProductDb();

//        ProductDb productDb = new ProductDb();
//
//        Product p1 = new Product("milk", "food", 100, 23.50);
//        productDb.create(p1);
//
//        Product found = productDb.getById(1);
//        System.out.println(found);
//
//        found.setPrice(22.0);
//        productDb.update(found);
//
//        List<Product> res = productDb.getAll("milk", "food", 10, 100, 10.0, 30.0);
//        for (Product p : res) {
//            System.out.println(p);
//        }
//
//        productDb.delete(found.getId());
//        System.out.println(found.getId());

        ProductService service = new ProductService();
        service.addProduct(new Product("apple", "fruit", 34, 12.99));
        List<Product> sorted = service.getSortedProducts("price", true);
        sorted.forEach(System.out::println);
    }
}
