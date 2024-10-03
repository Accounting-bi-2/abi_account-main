package bi.accounting.client;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

@Client("https://api.xero.com")
public interface XeroAPI {

    @Get("/connections")
    @Header(name = "Content-Type", value = "application/x-www-form-urlencoded")
    HttpResponse<List<HashMap<String, Object>>> getTenants(@Header("Authorization") String authorization);

}

