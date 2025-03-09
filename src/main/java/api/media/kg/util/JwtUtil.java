package api.media.kg.util;

import api.media.kg.dto.JwtResponseDTO;
import api.media.kg.enums.ProfileRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class JwtUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
    private static final String secretKey = "veryLongSecretmazgillattayevlasharaaxmojonjinnijonsurbetbekkiydirhonuxlatdibekloxovdangasabekochkozjonduxovmashaynikmaydagapchishularnioqiganbolsangizgapyoqaniqsizmazgi";
    public static String encode(Long id, List<ProfileRole> roleList) {
        String strRoles = roleList.stream().map(Enum::name)
                .collect(Collectors.joining(","));
        Map<String, String> claims = new HashMap<>();
        claims.put("roles",strRoles);
        return Jwts
                .builder()
                .setSubject(String.valueOf(id))
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getSignInKey())
                .compact();
    }
    public static JwtResponseDTO decode(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long id = Long.valueOf(claims.getSubject());
        String strRole = claims.get("roles").toString();
       List<ProfileRole> roleList = Arrays.stream(strRole.split(",")).map(ProfileRole::valueOf).toList();
        return new JwtResponseDTO(id,roleList);

    }



    public static String encode(Long id) {
        return Jwts
                .builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (60*60*1000)))
                .signWith(getSignInKey())
                .compact();
    }

    public static Long decodeRegVerToken(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());

    }

    private static SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
