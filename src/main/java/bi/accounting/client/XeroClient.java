package bi.accounting.client;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Client("https://identity.xero.com")
public interface XeroClient {

    @Post("/connect/token")
    @Header(name = "Content-Type", value = "application/x-www-form-urlencoded")
    HttpResponse<HashMap<?, ?>> idToken(@Header("Authorization") String authorization,
                                        @Body String idTokenRequest);

}

