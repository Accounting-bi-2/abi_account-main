package bi.accounting.repository;

import bi.accounting.model.Account;
import bi.accounting.model.AccountOauth;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface AccountOauthRepository extends CrudRepository<AccountOauth, Long> {
    AccountOauth findByAccountId(Long accountId);
    List<AccountOauth> findAllByIsDeleted(Boolean isDeleted);
    Optional<AccountOauth> findByAccountIdAndIsDeletedFalse(Long accountId);

}