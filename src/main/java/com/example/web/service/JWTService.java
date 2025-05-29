package com.example.web.service;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    // Xác thực chữ ký của token và trích xuất username
    public String extractUsername(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);  // Giải mã token thành JWSObject

            // Xác thực chữ ký
            if (!jwsObject.verify(new MACVerifier(SIGNER_KEY))) {
                throw new RuntimeException("Invalid token signature");
            }

            // Lấy claims từ payload của token
            JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());

            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting username from token", e);
        }
    }

    // Xác thực chữ ký của token và trích xuất scope
    public String extractScope(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);

            // Xác thực chữ ký
            if (!jwsObject.verify(new MACVerifier(SIGNER_KEY))) {
                throw new RuntimeException("Invalid token signature");
            }

            // Lấy claims từ payload của token
            JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());

            return claims.getStringClaim("scope");
        } catch (Exception e) {
            throw new RuntimeException("Error extracting scope from token", e);
        }
    }

    // Phương thức kiểm tra token có hợp lệ không
    public boolean validateToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);

            // Xác thực chữ ký
            return jwsObject.verify(new MACVerifier(SIGNER_KEY));
        } catch (Exception e) {
            return false;
        }
    }
}
