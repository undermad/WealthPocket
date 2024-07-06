package ectimel.security.jwt;
import ectimel.exceptions.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;


@Component
public class JwtTokenProvider {
    

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    
    @Value("${app-jwt-expiration-millisecond}")
    private String jwtExpirationTime;

    public String generateToken(String email) {
        Date expirationDate = Date.from(Instant.now().plusMillis(Long.parseLong(jwtExpirationTime)));
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(key())
                .compact();
    }


    private SecretKey key() {
        // decode secret key and return as SecretKey class
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getSubject(String jwt) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        return claims.getSubject();
    }



    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException("Invalid token");
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Expired token");
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("JWT claims string is empty.");
        }

        return true;
    }
    
}
