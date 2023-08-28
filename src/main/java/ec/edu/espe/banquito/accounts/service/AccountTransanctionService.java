package ec.edu.espe.banquito.accounts.service;

import ec.edu.espe.banquito.accounts.controller.req.AccountReqDto;
import ec.edu.espe.banquito.accounts.controller.req.AccountTransactionReqDto;
import ec.edu.espe.banquito.accounts.controller.res.AccountTransactionResDto;
import ec.edu.espe.banquito.accounts.model.Account;
import ec.edu.espe.banquito.accounts.model.AccountTransaction;
import ec.edu.espe.banquito.accounts.repository.AccountRepository;
import ec.edu.espe.banquito.accounts.repository.AccountTransactionRepository;
import ec.edu.espe.banquito.accounts.service.mapper.AccountTransactionMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AccountTransanctionService {
    private final AccountTransactionMapper accountTransactionMapper;
    private final AccountTransactionRepository accountTransactionRepository;
    private final AccountRepository accountRepository;

    public List<AccountTransactionResDto> findByAccountsTransactionByClientUK(String accountUK){
        List<AccountTransactionResDto> accountTransactionList=this.accountTransactionMapper.toRes(this.accountTransactionRepository.findValidByAccountUniqueKeyOrderByBookingDateDesc(accountUK));
        if(accountTransactionList.isEmpty()){
            log.error("No transactions in this account");
            throw new RuntimeException("No transactions in this account");
        }

        return accountTransactionList;
    }

    public List<AccountTransactionResDto> findTransactionsByDateRange(String accountUK, Date startDate, Date endDate) {
        List<AccountTransactionResDto> accountTransactionList = this.accountTransactionMapper.toRes(
            this.accountTransactionRepository.findValidByAccountUniqueKeyAndBookingDateBetweenOrderByBookingDateDesc(accountUK, startDate, endDate)
        );

        if (accountTransactionList.isEmpty()) {
            log.error("No transactions in this account within the specified date range");
            throw new RuntimeException("No transactions in this account within the specified date range");
        }

        return accountTransactionList;
    }

    public AccountTransactionResDto bankTransfer(AccountTransactionReqDto accountTransactionReqDto){
        String reference = generarNumeroReferencia();
        AccountTransaction accountTransaction=new AccountTransaction();
        Optional<Account> accountDebtorTmp=this.accountRepository.findValidByCodeInternalAccount(accountTransactionReqDto.getDebtorAccount());
        Double ammountTmp=accountTransactionReqDto.getAmmount().doubleValue();
        AccountTransaction accountTransactionDebtor = AccountTransaction.builder().build();
        switch (accountTransactionReqDto.getTransactionType()){
            case "TRANSFER":
                Optional<Account> accountCredtorTmp=this.accountRepository.findValidByCodeInternalAccount(accountTransactionReqDto.getCreditorAccount());
                if(accountDebtorTmp.isPresent() && accountCredtorTmp.isPresent()){

                    Double ammountDebtorTemp=accountDebtorTmp.get().getAvailableBalance().doubleValue();
                    Double resultDebtor=ammountDebtorTemp-ammountTmp;
                    System.out.println(resultDebtor);
                    accountDebtorTmp.get().setAvailableBalance(BigDecimal.valueOf(resultDebtor));
                    accountDebtorTmp.get().setTotalBalance(BigDecimal.valueOf(resultDebtor));

                    Double ammountCredtorTemp=accountCredtorTmp.get().getAvailableBalance().doubleValue();
                    Double resultCredtor=ammountCredtorTemp +ammountTmp;
                    System.out.println(resultCredtor);
                    accountCredtorTmp.get().setAvailableBalance(BigDecimal.valueOf(resultCredtor));
                    accountCredtorTmp.get().setTotalBalance(BigDecimal.valueOf(resultCredtor));

                     accountTransactionDebtor=AccountTransaction.builder()
                            .uniqueKey(UUID.randomUUID().toString())
                            .transactionType(AccountTransaction.TransactionType.TRANSFER)
                            .reference(reference)
                            .ammount((BigDecimal.valueOf(ammountTmp*-1)))
                            .balanceAfterTransaction(BigDecimal.valueOf(resultDebtor))
                            .creditorAccount(accountTransactionReqDto.getCreditorAccount())
                            .creditorBankCode(accountTransactionReqDto.getCreditorBankCode())
                            .debtorAccount(accountTransactionReqDto.getDebtorAccount())
                            .debtorBankCode(accountTransactionReqDto.getDebtorBankCode())
                            .creationDate(new Date())
                            .createdAt(new Date())
                            .bookingDate(new Date())
                            .valueDate(new Date())
                            .applyTax(false)
                            .state(AccountTransaction.State.ACT)
                            .notes(accountTransactionReqDto.getNotes())
                            .account(accountDebtorTmp.get())
                            .valid(true)
                            .build();

                    

                    AccountTransaction accountTransactionCredtor=AccountTransaction.builder()
                            .uniqueKey(UUID.randomUUID().toString())
                            .transactionType(AccountTransaction.TransactionType.TRANSFER)
                            .reference(reference)
                            .ammount((BigDecimal.valueOf(ammountTmp)))
                            .balanceAfterTransaction(BigDecimal.valueOf(resultCredtor))
                            .creditorAccount(accountTransactionReqDto.getCreditorAccount())
                            .creditorBankCode(accountTransactionReqDto.getCreditorBankCode())
                            .debtorAccount(accountTransactionReqDto.getDebtorAccount())
                            .debtorBankCode(accountTransactionReqDto.getDebtorBankCode())
                            .creationDate(new Date())
                            .createdAt(new Date())
                            .bookingDate(new Date())
                            .valueDate(new Date())
                            .applyTax(false)
                            .state(AccountTransaction.State.ACT)
                            .notes(accountTransactionReqDto.getNotes())
                            .account(accountCredtorTmp.get())
                            .valid(true)
                            .build();

                    

                    this.accountTransactionRepository.save(accountTransactionDebtor);
                    this.accountTransactionRepository.save(accountTransactionCredtor);
                    this.accountRepository.save(accountCredtorTmp.get());
                    this.accountRepository.save(accountDebtorTmp.get());

                }
                break;
            case "LOAN_REPAID":
                Optional<Account> accountBanQTmp=this.accountRepository.findValidByCodeInternalAccount(accountTransactionReqDto.getCreditorAccount());
                if(accountDebtorTmp.isPresent()){

                    Double ammountDebtorTemp=accountDebtorTmp.get().getAvailableBalance().doubleValue();
                    Double resultDebtor=ammountDebtorTemp-ammountTmp;
                    accountDebtorTmp.get().setAvailableBalance(BigDecimal.valueOf(resultDebtor));
                    accountDebtorTmp.get().setTotalBalance(BigDecimal.valueOf(resultDebtor));

                    Double ammountCredtorTemp=accountBanQTmp.get().getAvailableBalance().doubleValue();
                    Double resultCredtor=ammountCredtorTemp +ammountTmp;
                    System.out.println(resultCredtor);
                    accountBanQTmp.get().setAvailableBalance(BigDecimal.valueOf(resultCredtor));
                    accountBanQTmp.get().setTotalBalance(BigDecimal.valueOf(resultCredtor));

                     accountTransactionDebtor=AccountTransaction.builder()
                            .uniqueKey(UUID.randomUUID().toString())
                            .transactionType(AccountTransaction.TransactionType.LOAN_REPAID)
                            .reference(reference)
                            .ammount((BigDecimal.valueOf(ammountTmp*-1)))
                            .balanceAfterTransaction(BigDecimal.valueOf(resultDebtor))
                            .creditorAccount(accountTransactionReqDto.getCreditorAccount())
                            .creditorBankCode(accountTransactionReqDto.getCreditorBankCode())
                            .debtorAccount(accountTransactionReqDto.getDebtorAccount())
                            .debtorBankCode(accountTransactionReqDto.getDebtorBankCode())
                            .creationDate(new Date())
                            .createdAt(new Date())
                            .bookingDate(new Date())
                            .valueDate(new Date())
                            .applyTax(false)
                            .state(AccountTransaction.State.ACT)
                            .notes(accountTransactionReqDto.getNotes())
                            .account(accountDebtorTmp.get())
                            .valid(true)
                            .build();

                    

                    AccountTransaction accountTransactionCredtor=AccountTransaction.builder()
                            .uniqueKey(UUID.randomUUID().toString())
                            .transactionType(AccountTransaction.TransactionType.LOAN_REPAID)
                            .reference(reference)
                            .ammount((BigDecimal.valueOf(ammountTmp)))
                            .balanceAfterTransaction(BigDecimal.valueOf(resultCredtor))
                            .creditorAccount(accountTransactionReqDto.getCreditorAccount())
                            .creditorBankCode(accountTransactionReqDto.getCreditorBankCode())
                            .debtorAccount(accountTransactionReqDto.getDebtorAccount())
                            .debtorBankCode(accountTransactionReqDto.getDebtorBankCode())
                            .creationDate(new Date())
                            .createdAt(new Date())
                            .bookingDate(new Date())
                            .valueDate(new Date())
                            .applyTax(false)
                            .state(AccountTransaction.State.ACT)
                            .notes(accountTransactionReqDto.getNotes())
                            .account(accountBanQTmp.get())
                            .valid(true)
                            .build();

                    

                    this.accountTransactionRepository.save(accountTransactionDebtor);
                    this.accountTransactionRepository.save(accountTransactionCredtor);
                    this.accountRepository.save(accountBanQTmp.get());
                    this.accountRepository.save(accountDebtorTmp.get());
                    
                }
                break;
                
        }
        
        
        
        return  this.accountTransactionMapper.toRes(accountTransactionDebtor);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String generarNumeroReferencia() {
        Long nextValue = jdbcTemplate.queryForObject("SELECT nextval('referencia_seq')", Long.class);
        return String.format("%08d", nextValue); 
    }
}
