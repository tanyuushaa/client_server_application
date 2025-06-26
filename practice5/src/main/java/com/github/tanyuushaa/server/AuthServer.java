package com.github.tanyuushaa.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.tanyuushaa.auth.AuthService;
import com.github.tanyuushaa.core.Product;
import com.github.tanyuushaa.process.ProductService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class AuthServer {
    public static void main(String[] args) {
        port(8080);

        Algorithm algorithm = Algorithm.HMAC256("secret");

        before("/api/*", (request, response) -> {
            String authHeader = request.headers("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                halt(403, "Forbidden: No token");
            }

            String token = authHeader.substring("Bearer ".length());
            try {
                JWT.require(algorithm).build().verify(token);
            } catch (Exception e) {
                halt(403, "Forbidden: Invalid token");
            }
        });

        post("/login", ((request, response) -> {
            Map<String, String> body = new Gson().fromJson(request.body(), Map.class);
            String login = body.get("login");
            String password = body.get("password");

            try {
                String token = AuthService.login(login, password);
                response.status(200);
                response.type("application/json");

                Map<String, String> result = new HashMap<>();
                result.put("token", token);
                return new Gson().toJson(result);
            } catch (RuntimeException e) {
                response.status(401);
                response.type("application/json");
                return new Gson().toJson(Map.of("error", "Unauthorized"));
            }
        }));

        get("/api/good/test", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Map.of("message", "Access granted"));
        });

        ProductService productService = new ProductService();
//        Product newProduct = new Product("banana", "fruit", 100, 25.5);
//        productService.addProduct(newProduct);

        get("/api/good/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                Product product = productService.getProductById(id);
                if (product != null) {
                    res.status(200);
                    res.type("application/json");
                    return new Gson().toJson(product);
                } else {
                    res.status(404);
                    return "Not Found";
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return "Invalid ID";
            }
        });

        put("/api/good", (req, res) -> {
            res.type("application/json");
            try {
                Product product = new Gson().fromJson(req.body(), Product.class);

                if (product.getPrice() <= 0 || product.getQuantity() < 0 ||product.getName() == null) {
                    res.status(409);
                    return new Gson().toJson(Map.of("error", "Invalid product"));
                }
                ProductService productService1 = new ProductService();
                productService1.addProduct(product);

                res.status(201);
                return new Gson().toJson(Map.of("id", product.getId()));

            } catch (Exception e) {
                res.status(400);
                return new Gson().toJson(Map.of("error", "Bad Request"));
            }
        });

        post("/api/good/:id", (req, res) -> {
            res.type("application/json");
            try {
                int id = Integer.parseInt(req.params("id"));
                Product existing = productService.getProductById(id);
                if (existing == null) {
                    res.status(404);
                    return "Not Found";
                }

                Product update = new Gson().fromJson(req.body(), Product.class);

                if (update.getName() == null || update.getPrice() <= 0 || update.getQuantity() < 0) {
                    res.status(409);
                    return new Gson().toJson(Map.of("error", "Invalid data"));
                }

                existing.setName(update.getName());
                existing.setCategory(update.getCategory());
                existing.setPrice(update.getPrice());
                existing.setQuantity(update.getQuantity());

                productService.updateProduct(existing);
                res.status(204);
                return " ";
            } catch (NumberFormatException e) {
                res.status(400);
                return "Invalid ID";
            } catch (Exception e) {
                res.status(500);
                return new Gson().toJson(Map.of("error", "Internal Server Error"));
            }
        });

        delete("/api/good/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Product product = productService.getProductById(id);
                if (product == null) {
                    res.status(404);
                    return "Not Found";
                }
                productService.deleteProductById(id);
                res.status(204);
                return "";
            } catch (NumberFormatException e) {
                res.status(400);
                return "Invalid ID";
            }
        });


    }




}
