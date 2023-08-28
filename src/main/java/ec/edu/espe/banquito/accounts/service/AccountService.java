package ec.edu.espe.banquito.accounts.service;

import ec.edu.espe.banquito.accounts.controller.req.AccountReqDto;
import ec.edu.espe.banquito.accounts.controller.res.AccountResDto;
import ec.edu.espe.banquito.accounts.exception.CustomException;
import ec.edu.espe.banquito.accounts.model.Account;
import ec.edu.espe.banquito.accounts.repository.AccountRepository;
import ec.edu.espe.banquito.accounts.service.mapper.AccountMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.security.auth.login.AccountNotFoundException;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public Account findByUK(Integer accountId){
        return this.accountRepository.findValidById(accountId).orElseThrow(()->{
            log.error("Account with id {} not found", accountId);
            return new CustomException("Account with id "+accountId+"not found",
                    "NOT_FOUND",
                    404);
        });
    }

    public List<AccountResDto> findAccountsByClientUK(String clientUK){
        List<AccountResDto> accountList=this.accountMapper.toRes(this.accountRepository.findByClientUk(clientUK));

        if(accountList.isEmpty()){
            log.error("Client does not have accounts");
            throw new RuntimeException("Client does not have accounts");
        }else{
            log.info("Ingreso exitoso");
        }
        return accountList;

    }


    public AccountResDto findAccountByAccountUK(String accountUK){
        return this.accountMapper.toRes(this.accountRepository.findValidByUK(accountUK).orElseThrow(()->{
            log.error("Account with id {} not found", accountUK);
            return new RuntimeException("Account with id "+accountUK+"not found");
        }));
    }
    public AccountResDto findAccountByInternalCodeAccount(String internalCodeAccount){
        return this.accountMapper.toRes(this.accountRepository.findValidByCodeInternalAccount(internalCodeAccount).orElseThrow(()->{
            log.error("Account with id {} not found", internalCodeAccount);
            return new RuntimeException("Account with id "+internalCodeAccount+"not found");
        }));
    }

    

    @Transactional
    public AccountResDto updateMaxOverdraft(String accountUK, BigDecimal maxOverdraft) {
        Optional<Account> account = accountRepository.findValidByUK(accountUK);
        
        if(account.isPresent()){
            account.get().setMaxOverdraft(maxOverdraft);
            this.accountRepository.save(account.get());
        }
        return this.accountMapper.toRes(account.get());
    }

    public void createAccount(AccountReqDto accountRQ){
        String uniqueKey = UUID.randomUUID().toString();
        Account account = this.accountMapper.toReq(accountRQ);
        account.setUniqueKey(uniqueKey);
        account.setActivationDate(new Date());
        account.setCreatedAt(new Date());
        account.setLastInterestCalculationDate(new Date());
        
        this.accountRepository.save(account);
    }



}
