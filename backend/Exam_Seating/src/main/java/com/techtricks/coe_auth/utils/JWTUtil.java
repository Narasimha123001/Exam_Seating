package com.techtricks.coe_auth.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {


    private final long EXPIRATION_TIME = 1000 * 60 * 60;
    private final String SECRET = "my-super-secret-key-that-isCOE-Narasimha-Satwika-@1417-October";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());



    public String generateToken(String email , String role , Long  registerNumber) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key , SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims  extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String extractEmail(String token){
        return extractClaims(token)
                .getSubject();
    }

    public String extractRole(String token){
        return extractClaims(token)
                .get("role").toString();
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

//    public boolean validateToken(String email , UserDetails userDetails , String token){
//
//        String username = userService.findByEmail(email).getUsername();
//
//        return username.equals(userDetails.getUsername()) &&  !isTokenExpired(token);
//
//    }

//    public boolean validateToken(String token) {
//        return !isTokenExpired(token);
//    }


    public boolean validateToken(String username, UserDetails userdetails , String token) {

        return username.equals(userdetails.getUsername()) && !isTokenExpired(token);

    }

}
