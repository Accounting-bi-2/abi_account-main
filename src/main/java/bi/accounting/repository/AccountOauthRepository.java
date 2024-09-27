package bi.accounting.repository;

import bi.accounting.model.AccountOauth;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface AccountOauthRepository extends CrudRepository<AccountOauth, Long> {
    AccountOauth findByAccountId(Long accountId);
}