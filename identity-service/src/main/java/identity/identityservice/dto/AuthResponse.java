package identity.identityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private final String tokenType = "Bearer";



}
