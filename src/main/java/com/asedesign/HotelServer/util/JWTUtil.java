package com.asedesign.HotelServer.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {
//    private String generateToken(Map<String,Object> extraClaims, UserDetails details) {
//        return Jwts.builder()
//        .setClaims(extraClaims)
//        .setSubject(details.getUsername())
//         .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
//                .signWith(SignatureAlgorithm.HS256, getSigninKey()).compact();
//    }
    private String generateToken(Map<String, Object> extraClaims, UserDetails details) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(details.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 day
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
       return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);

        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build().parseClaimsJwt(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public String extractUserName(String token){
        return  extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode("Z+LqDzTjYqf2ZXTlQ/eTBWOEXBdB7SL9CgvPz+ClHcA=");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
