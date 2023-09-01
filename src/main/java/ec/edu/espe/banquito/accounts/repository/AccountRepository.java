package ec.edu.espe.banquito.accounts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.edu.espe.banquito.accounts.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    @Query("Select ac from Account  ac " +
            "where (ac.valid=true) and ac.uniqueKey=:accountUk")
    Optional<Account>findValidByUniqueKey(String accountUk);

    @Query("SELECT  ac from Account ac " +
            "where (ac.valid=true) and ac.clientUk=:clientUK")
    List<Account>findByClientUk(@Param("clientUK") String clientUK);

    @Query("Select ac from Account  ac " +
            "where (ac.valid=true) and ac.uniqueKey=:accountUK")
    Optional<Account>findValidByUK(@Param("accountUK") String accountUK);

    @Query("Select ac from Account  ac " +
            "where (ac.valid=true) and ac.codeInternalAccount=:internalAccountCode")
    Optional<Account>findValidByCodeInternalAccount(@Param("internalAccountCode") String internalAccountCode);

}
