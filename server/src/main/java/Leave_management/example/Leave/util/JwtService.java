package Leave_management.example.Leave.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "8d1247f44af7e472bbce1234567890ab8d1247f44af7e472bbce1234567890ab";

    // Get the key used for signing the JWT
    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract a specific claim from the JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract admin ID from the JWT token (corrected method)
    public String extractAdminId(String token) {
        return extractClaim(token, claims -> claims.get("adminId", String.class));  // assuming 'adminId' is stored in the JWT
    }

    // Generate a JWT token for a given username with extra claims
    public String generateToken(String username, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 1 day expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate the token by comparing username and checking expiration
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check if token has a specific role (supporting both single and list roles)
    public Boolean hasRole(String token, String role) {
        Claims claims = extractAllClaims(token);
        Object rolesClaim = claims.get("roles");

        if (rolesClaim instanceof List<?>) {
            // If roles are stored as a list, check if the list contains the role
            List<String> roles = (List<String>) rolesClaim;
            return roles != null && roles.contains(role);
        } else if (rolesClaim instanceof String) {
            // If only a single role is stored as a string, check if it matches the role
            return role.equals(rolesClaim);
        }
        return false;
    }

    // Refresh token (if needed)
    public String refreshToken(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();
        Map<String, Object> extraClaims = new HashMap<>();
        return generateToken(username, extraClaims);
    }

    // Validate if a token is valid (check for expiration and structure)
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Get the username from a token
    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }
}
