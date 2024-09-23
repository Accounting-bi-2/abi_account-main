package bi.accounting.controller;

import bi.accounting.dto.AccountDTO;
import bi.accounting.repository.AccountRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/health")
@Secured(SecurityRule.IS_ANONYMOUS)  // Ensure only authenticated users can access
public class HealthController {

    @Get(uri = "/", produces = "text/plain")
    public String index() {
        return "OK";
    }


}
