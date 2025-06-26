package com.github.tanyuushaa.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Base64;
import java.util.Date;

public class AuthService {
    private static final String SECRET = "secret";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier verifier = JWT.require(algorithm).build();

    public static String login(String login, String password) {
        if ("user".equals(login) && "pass".equals(password)) {
            return generateToken(login);
        } else {
            throw new RuntimeException("Invalid login or password");
        }
    }

    private static String generateToken(String login) {
        return JWT.create()
                .withSubject(login)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600_000))
                .sign(algorithm);
    }

    public static boolean isTokenValid(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String token = login("user", "password");
        System.out.println(token);

        boolean isValid = isTokenValid(token);
        System.out.println(isValid);

    }
}
