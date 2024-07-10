package com.qq89985229.openwechat.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String secret = "Abc123!@#";
    private static final Algorithm alg = Algorithm.HMAC256(secret);

    /**
     * 生成token
     */
    public static String create(Map<String, String> claims, long expire) {
        try {
            JWTCreator.Builder builder = JWT.create()
                    .withHeader(Map.of("Type", "Jwt", "alg", "HS256"))
                    .withIssuer("auth0")
                    .withExpiresAt(new Date(new Date().getTime() + expire));
            claims.forEach(builder::withClaim);
            return builder.sign(alg);
        } catch (JWTCreationException e) {
            return null;
        }
    }

    /**
     * 验证token
     */
    public static boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(alg).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    /**
     * 解密token
     */
    public static String decode(String token, String claim) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

}
