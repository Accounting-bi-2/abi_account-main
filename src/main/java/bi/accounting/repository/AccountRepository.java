package bi.accounting.repository;


import bi.accounting.model.Account;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface AccountRepository extends CrudRepository<Account, Long> {

    List<Account> findByUserIdAndIsDeletedFalse(Long userId);
    List<Account> findByUserId(Long userId);
    Account findByOrgId(String orgId);

}