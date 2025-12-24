package com.moviebooking.identity_service.service;
import com.moviebooking.identity_service.dto.AuthResponse;
import com.moviebooking.identity_service.dto.UserLoginRequest;
import com.moviebooking.identity_service.dto.UserRegisterRequest;
import com.moviebooking.identity_service.entity.UserCredential;
import com.moviebooking.identity_service.repository.UserRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    public String register(UserRegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserCredential user = new UserCredential();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return "Đăng ký thành công!";
    }

    public AuthResponse login(UserLoginRequest request) {

        UserCredential user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new RuntimeException("Sai mật khẩu!");
        }


        String token = generateToken(request.getUsername());

        return new AuthResponse(token, true);
    }

    private String generateToken(String username) {
        // A. Tạo Header (Thuật toán HS256)
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // B. Tạo Body (Claims - Nội dung tấm vé)
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username) // Vé này của ai?
                .issuer("movie-booking.com") // Ai cấp?
                .issueTime(new Date()) // Cấp lúc nào?
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                )) // Hết hạn sau 1 giờ
                .claim("customClaim", "Custom Data") // Thêm data tùy ý
                .build();

        // C. Ký tên (Signature)
        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo token", e);
        }
    }
}