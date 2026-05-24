package identity.identityservice.service;

import identity.identityservice.config.JwtProperties;
import identity.identityservice.dto.AuthResponse;
import identity.identityservice.entity.RefreshToken;
import identity.identityservice.entity.User;
import identity.identityservice.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.UUID;

public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;


    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));


        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()))
                .build();

        return refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        // So sánh thời gian hết hạn với thời điểm hiện tại
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token); // Dọn dẹp token hết hạn khỏi DB
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public AuthResponse rotateRefreshToken(String requestToken) {
        RefreshToken oldRefreshToken = refreshTokenRepository.findByTokenWithUser(requestToken)
                .orElseThrow(() -> new TokenRefreshException(requestToken, "Refresh token không tồn tại trên hệ thống!"));

        if (oldRefreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(oldRefreshToken); // Xóa token hết hạn khỏi DB
            throw new TokenRefreshException(requestToken, "Refresh token đã hết hạn. Vui lòng đăng nhập lại!");
        }

        User user = oldRefreshToken.getUser();

        refreshTokenRepository.delete(oldRefreshToken);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()))
                .build();
        refreshTokenRepository.save(newRefreshToken);

        String newAccessToken = jwtUtils.generateAccessToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }



}
