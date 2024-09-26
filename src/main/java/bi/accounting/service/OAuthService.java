package bi.accounting.service;
import jakarta.inject.Singleton;
import java.security.SecureRandom;
import java.util.Base64;

@Singleton
public class OAuthService {

    public String generateState() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}