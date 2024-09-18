package bi.accounting.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtGenerator {

    private static final String SECRET_KEY = "pleaseChangeThisSecretForANewOne12345678901234567890"; // This should match your JWT secret key

    public static String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static void main(String[] args) {
        // Generate a token for testing
        String token = generateToken("2"); // Example user ID
        System.out.println("Generated Token: " + token);
    }
}