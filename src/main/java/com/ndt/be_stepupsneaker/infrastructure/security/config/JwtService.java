package com.ndt.be_stepupsneaker.infrastructure.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    //    private static final String SECRET_KEY = "";
    private static final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);

    // Tạo JWT từ thông tin người dùng
    // UserDetails là một giao diện Spring security chứa thông tin về người dùng
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Tạo một chuỗi JWT với claims bổ sung và UserDetail chứa thông tin người dùng
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        PrivateKey privateKey = keyPair.getPrivate(); // một cặp khóa (public key or private key ) kí và xác minh JWT
        return Jwts
                .builder()
                .setClaims(extraClaims) //
                .setSubject(userDetails.getUsername())// set name người dùng
                .setIssuedAt(new Date(System.currentTimeMillis())) // Đặt thời điểm bắt đầu của JWT
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) //Thời điểm hết hạn của JWT
                .signWith(privateKey, SignatureAlgorithm.ES256)// Tạo JWT bằng thuật toán ECDSA với private key
                .compact();// Kết thúc quá trình xây dụng JWT  và trả về chuỗi JWT hoàn chỉnh

    }

    // Trích xuất thông tin từ một claim cụ thể trong chuỗi JWT sử dụng một ClaimsResolver
    // Function<Claims, T> claimsResolver : Hàm chuyển đổi Claims sang đối tượng T
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Kiểm tra chuỗi JWT hợp lệ hay không từ người dùng cung cấp(tên và hết hạn)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Trích xuất tên người dùng từ chuỗi JWT
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra chuỗi JWT hết hạn hay không
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Trích xuất thời điểm hết hạn của JWT
    // Output : Thời điểm hết hạn (kiểu Date)
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // Trả về đối tượng Claims
    // Claims chứa tất cả các claims có trong chuỗi JWT
    // claims chứa tên người dùng, thời gian hết hạn,...
    private Claims extractAllClaims(String token) {
        PublicKey publicKey = keyPair.getPublic();
        return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    // Cấu trúc JWT : 3 phần (HEADER , PAYLOAD , SIGNATURE)
    // HEADER : chứa 2 phần ( alg và typ)
    // alg : Xác định thuật toán mã hóa cho chuỗi JWT như : HS256,ES256,...
    // typ : Chỉ ra đối tượng JWT
    // PAYLOAD : Chứa thông tin người dùng đặt trong chuỗi Token, ví dụ như name,id,role,...
    // SIGNATURE : Tạo ra JWT bằng cách mã hóa phần HEADER và PAYLOAD kèm theo mội cặp chuỗi secret

}
