package com.djuber.djuberbackend.Infastructure.Security;

import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.TokenExpiredException;
import com.djuber.djuberbackend.Infastructure.Util.DateCalculator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTGenerator {

    final DateCalculator dateCalculator;

    public Pair<String, Date> generateToken(Authentication authentication) {
        String username = authentication.getName();
        return Pair.of(Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(this.getExpiringDate())
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact(), this.getExpiringDate());
    }

    public Pair<String,Date> generateRefreshToken(String email){
        return Pair.of(Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(this.getExpiringDate())
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact(),this.getExpiringDate());
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token).getBody();
            return true;
        } catch (Exception ex) {
            throw new TokenExpiredException("JWT was expired or incorrect");
        }
    }

    private Date getExpiringDate() {
        return dateCalculator.getDate2HoursFromNow();
    }
}
