package bi.accounting.client;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("${openid-client.uri}")
public interface OpenIdClient {

    @Get
    Mono<String> getToken(@QueryValue("provider") String provider,
                          @QueryValue("code") String code);

}
