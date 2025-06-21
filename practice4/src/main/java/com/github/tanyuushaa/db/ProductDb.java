package com.github.tanyuushaa.db;

import com.github.tanyuushaa.core.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDb {
    public ProductDb() {
        try {
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS product(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                category TEXT,
                quantity INTEGER,
                price REAL
                );
                """;

        try (Connection connection = DbUtils.connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table created");
        }
    }

    public void create(Product product) {
        String sql = "INSERT INTO product(name, category, quantity, price) VALUES (?, ?, ?, ?)";
        try ( Connection connection = DbUtils.connect();
              PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setInt(3, product.getQuantity());
            statement.setDouble(4, product.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Product getById(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try (Connection connection = DbUtils.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    public void update(Product product) {
        String sql = "UPDATE product SET name = ?, category = ?, quantity = ?, price = ? WHERE id = ?";
        try (Connection connection = DbUtils.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setInt(3, product.getQuantity());
            statement.setDouble(4, product.getPrice());
            statement.setInt(5, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection connection = DbUtils.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<Product> getAll(
            String nameContains,
            String category,
            Integer minQuantity,
            Integer maxQuantity,
            Double minPrice,
            Double maxPrice
    ) {
        StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (nameContains != null && !nameContains.isEmpty()) {
            sql.append(" AND name LIKE ? ");
            params.add("%" + nameContains + "%");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ? ");
            params.add(category);
        }
        if (minQuantity != null) {
            sql.append(" AND quantity >= ? ");
            params.add(minQuantity);
        }
        if (maxQuantity != null) {
            sql.append(" AND quantity <= ? ");
            params.add(maxQuantity);
        }
        if (minPrice != null) {
            sql.append(" AND price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ? ");
            params.add(maxPrice);
        }
        try (Connection connection = DbUtils.connect();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            List<Product> res = new ArrayList<>();
            while (resultSet.next()) {
                res.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price")
                ));
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }


    }
    public void deleteAll() {
        try (Connection connection = DbUtils.connect(); Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM product");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
