package bi.accounting;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OpenAPIDefinition(
        info = @Info(
                title = "abi_accounts",
                version = "0.0"
        )
)
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        // Log application start
        LOG.info("Starting application...");

        // Log Java version
        String javaVersion = System.getProperty("java.version");
        LOG.info("Java Version: {}", javaVersion);

        // Log environment variables
        System.getenv().forEach((key, value) -> LOG.info("Environment Variable: {} = {}", key, value));

        // Start the Micronaut application
        Micronaut.run(Application.class, args);
    }
}
