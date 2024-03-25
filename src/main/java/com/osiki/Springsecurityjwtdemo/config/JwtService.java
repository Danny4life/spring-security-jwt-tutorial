package com.osiki.Springsecurityjwtdemo.config;

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
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // site to generate key --  https://jwt-keys.21no.de/

  // private final static String SECRET_KEY = "aOV/I/Yto++km+DhkK749xlQZmS+xCknj/Cq3OKg3sCM2jE1X4RtrqxAByHVNESbEfD3Y4FXid0kPhdxvXhiHCg9AHY0s0V9R2OqBlAif/Ci1qAEU4NieNfMKSKms9V/X+pxaJeCcbfyODVDVOcBKm6UvG/RspKSNmZD14iBvPf6OmjdWLwk8XHfOwsHneJ0KoaW71a6GedAVSlvbrMpe6+6GrGv8N2J5+BWuSBplsQDL9iTofU9C7waOce/DS+39jX2lKZ+sDUDfTzNCuDXtnkcGYk+9Relh6j5lGm971pMxleAuAu0Xzd/a7UYpQOwuVmEuUs+T30krfGtq3JkXQ5jcQSmFfr4uszijW3TTF8=";

    private final static String SECRET_KEY = "ebkFejbn46IhIHUttrWcy+7WHarcct5v3C0ia5QAb93xZR7woNG6nPSdxBKdFZ8hm8VZ4kQ8RRB6XPg4jb/I3A==";


    //extract all claims
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
       byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // extract single claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);

    }

    public String extractUsername(String token){

        return extractClaim(token, Claims::getSubject);
    }

    // generate token for claims and user details
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails){

        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // generate token for user details
    public String generateToken(UserDetails userDetails){
        return (generateToken(new HashMap<>(), userDetails));
    }

    // check if token is valid
    public Boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

}
