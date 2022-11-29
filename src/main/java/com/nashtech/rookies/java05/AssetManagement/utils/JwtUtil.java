package com.nashtech.rookies.java05.AssetManagement.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String secretKey;
    private long accessTokenExpiredDate = 60 * 60 * 5 * 1000; //5 Hours

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiredDate))
                .withClaim("roles", user.getRole().getName())
                .sign(algorithm);

        return access_token;
    }

    public String getUsernameFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        return username;
    }

}
