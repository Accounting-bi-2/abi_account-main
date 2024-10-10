package bi.accounting.service;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Singleton
public class OAuthService {

    public String generateState(Long user) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);

        String randomString = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return randomString + "__" + user;
    }

    public String getBasicAuthHeader(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

}