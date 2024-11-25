package Leave_management.example.Leave.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class Jwtutil {

    // Load environment variables using dotenv
    private final String SECRET_KEY = Dotenv.load().get("JWT_SECRET_KEY");

    // Encode the secret key in Base64 (if needed)
    private String encodeSecretKey(String secretKey) {
        return Base64.getUrlEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(String email) {
        String encodedSecretKey = encodeSecretKey(SECRET_KEY);  // Ensure it's encoded correctly

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, encodedSecretKey)  // Use the encoded key here
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            // Decode the key and validate the token
            String encodedSecretKey = encodeSecretKey(SECRET_KEY);
            Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}