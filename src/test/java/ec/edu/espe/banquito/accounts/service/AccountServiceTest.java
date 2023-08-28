package ec.edu.espe.banquito.accounts.service;

import ec.edu.espe.banquito.accounts.controller.req.AccountReqDto;
import ec.edu.espe.banquito.accounts.controller.res.AccountResDto;
import ec.edu.espe.banquito.accounts.exception.CustomException;
import ec.edu.espe.banquito.accounts.model.Account;
import ec.edu.espe.banquito.accounts.repository.AccountRepository;
import ec.edu.espe.banquito.accounts.service.mapper.AccountMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class AccountServiceTest {
    @Mock
    private  AccountRepository accountRepository;
    @Mock
    private  AccountMapper accountMapper;
    @InjectMocks
    private AccountService accountService;
    @DisplayName("findByUK -- Success Scenario")
    @Test
    void Test_When_findByUK_Success() {
        //Mocking
        Account account = getMockAccount();


        when(accountRepository.findValidById(anyInt()))
                .thenReturn(Optional.of(account));

        //Actual
        Account acc = accountService.findByUK(1);
        System.out.println(acc.getId());
        //Verification
        verify(accountRepository, times(1)).findValidById(anyInt());

        //Assert
        Assertions.assertNotNull(acc);
        Assertions.assertEquals(account.getId(), acc.getId());
        System.out.println(acc.getId());
    }

    @DisplayName("findByUK -- Failure Scenario")
    @Test
    void test_when_findByUK_NOT_FOUND() {
        when(accountRepository.findValidById(anyInt())).thenReturn(Optional.ofNullable(null));

        CustomException exception = assertThrows(CustomException.class,
                () -> accountService.findByUK(1));
        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getStatus());

        verify(accountRepository, times(1)).findValidById(anyInt());
    }
/*
    @DisplayName("findAccountsByClientUK -- Success Scenario")
    @Test
    void Test_When_findAccountsByClientUK_Success() {
        // Arrange
        String clientUK = "205d8be5-69d2-4523-9681-1427652137be";

        // Mocking the behavior of the repository and mapper
        when(accountRepository.findByClientUk(clientUK)).thenReturn(Collections.emptyList());
        when(accountMapper.toRes(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<AccountResDto> result = accountService.findAccountsByClientUK(clientUK);

        // Assert
        assertTrue(result.isEmpty());
        verify(accountRepository, times(1)).findByClientUk(clientUK);
        verify(accountMapper, times(1)).toRes(Collections.emptyList());
    }
    */

    @DisplayName("findAccountByAccountUK -- Failure Scenario")
    @Test
    void Test_When_findAccountsByClientUK_Failure() {
// Arrange
        String clientUK = "205d8be5-69d2-4523-9681-1427652137be";
        List<Account> accountList=List.of(new Account());
        List<AccountResDto> accountResDtos =List.of(new AccountResDto()); // create corresponding DTOs

                        // Mocking the behavior of the repository and mapper
        when(accountRepository.findByClientUk(clientUK)).thenReturn(accountList);
        when(accountMapper.toRes(accountList)).thenReturn(accountResDtos);

        // Act
        List<AccountResDto> result = accountService.findAccountsByClientUK(clientUK);

        // Assert
        assertEquals(accountResDtos, result);
        verify(accountRepository, times(1)).findByClientUk(clientUK);
        verify(accountMapper, times(1)).toRes(accountList);
    }
    @DisplayName("findAccountByAccountUK -- Success Scenario")
    @Test
    void Test_When_findAccountByAccountUK_Success() {
        // Arrange
        String accountUK = "4f2cc37f-f38-4e0c-8s56-84770b89e5f7";
        Account account= new Account();
        AccountResDto accountResDto = new AccountResDto();// create the corresponding DTO

                // Mocking the behavior of the repository and mapper
        when(accountRepository.findValidByUK(accountUK)).thenReturn(Optional.of(account));
        when(accountMapper.toRes(account)).thenReturn(accountResDto);

        // Act
        AccountResDto result = accountService.findAccountByAccountUK(accountUK);

        // Assert
        assertEquals(accountResDto, result);
        verify(accountRepository, times(1)).findValidByUK(accountUK);
        verify(accountMapper, times(1)).toRes(account);
    }
    @DisplayName("findAccountByAccountUK -- Failure Scenario")
    @Test
    void Test_When_findAccountByAccountUK_Failure() {
        // Arrange
        String accountUK = "nonExistentAccountUK";

        // Mocking the behavior of the repository to return an empty optional
        when(accountRepository.findValidByUK(accountUK)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            accountService.findAccountByAccountUK(accountUK);
        });
        verify(accountRepository, times(1)).findValidByUK(accountUK);
        verify(accountMapper, never()).toRes((Account) any());
    }
    @DisplayName("findAccountByInternalCodeAccount -- Success Scenario")
    @Test
    void Test_When_findAccountByInternalCodeAccount_Success() {
        // Arrange
        String internalCodeAccount = "someInternalCodeAccount";
        Account accountEntity = new Account(); // create an account entity for testing
        AccountResDto accountResDto =new AccountResDto(); // create the corresponding DTO

        // Mocking the behavior of the repository and mapper
        when(accountRepository.findValidByCodeInternalAccount(internalCodeAccount)).thenReturn(Optional.of(accountEntity));
        when(accountMapper.toRes(accountEntity)).thenReturn(accountResDto);

        // Act
        AccountResDto result = accountService.findAccountByInternalCodeAccount(internalCodeAccount);

        // Assert
        assertEquals(accountResDto, result);
        verify(accountRepository, times(1)).findValidByCodeInternalAccount(internalCodeAccount);
        verify(accountMapper, times(1)).toRes(accountEntity);
    }
    @DisplayName("findAccountByInternalCodeAccount -- Failure Scenario")
    @Test
    void Test_When_findAccountByInternalCodeAccount_Failure() {
        // Arrange
        String internalCodeAccount = "nonExistentInternalCodeAccount";

        // Mocking the behavior of the repository to return an empty optional
        when(accountRepository.findValidByCodeInternalAccount(internalCodeAccount)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            accountService.findAccountByInternalCodeAccount(internalCodeAccount);
        });
        verify(accountRepository, times(1)).findValidByCodeInternalAccount(internalCodeAccount);
        verify(accountMapper, never()).toRes(anyList());
    }
    @DisplayName("updateMaxOverdraft -- Success Scenario")
    @Test
    void Test_When_updateMaxOverdraft_Success() {
        // Arrange
        String accountUK = "4f2cc37f-f38-4e0c-8s56-84770b89e5f7";
        BigDecimal newMaxOverdraft = BigDecimal.valueOf(1000);

        Account accountEntity = new Account(); // create an account entity for testing

        // Mocking the behavior of the repository
        when(accountRepository.findValidByUK(accountUK)).thenReturn(Optional.of(accountEntity));
        when(accountRepository.save(any())).thenReturn(accountEntity);

        // Mocking the behavior of the mapper
        AccountResDto accountResDto =new AccountResDto(); // create the corresponding DTO
        when(accountMapper.toRes(accountEntity)).thenReturn(accountResDto);

        // Act
        AccountResDto result = accountService.updateMaxOverdraft(accountUK, newMaxOverdraft);

        // Assert
        assertEquals(accountResDto, result);
        assertEquals(newMaxOverdraft, accountEntity.getMaxOverdraft());
        verify(accountRepository, times(1)).findValidByUK(accountUK);
        verify(accountRepository, times(1)).save(accountEntity);
        verify(accountMapper, times(1)).toRes(accountEntity);
    }
    @DisplayName("updateMaxOverdraft -- Failure Scenario")
    @Test
    void Test_When_updateMaxOverdraft_Failure() {
        // Arrange
        String accountUK = "nonExistentAccountUK";
        BigDecimal newMaxOverdraft = BigDecimal.valueOf(1000);

        // Mocking the behavior of the repository to return an empty optional
        when(accountRepository.findValidByUK(accountUK)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            accountService.updateMaxOverdraft(accountUK, newMaxOverdraft);
        });
        verify(accountRepository, times(1)).findValidByUK(accountUK);
        verify(accountRepository, never()).save(any());
        verify(accountMapper, never()).toRes(anyList());
    }
/*
    @DisplayName("createAccount -- Success Scenario")
    @Test
    void Test_When_createAccount_Success() {
        AccountReqDto accountReqDto = new AccountReqDto(); // create an AccountReqDto for testing
                Account accountEntity= new Account(); // create an account entity for testing

                String uniqueKey = UUID.randomUUID().toString();
        Date currentDate = new Date();

        // Mocking the behavior of the mapper
        when(accountMapper.toReq(accountReqDto)).thenReturn(accountEntity);

        // Act
        accountService.createAccount(accountReqDto);

        // Assert
        assertEquals(uniqueKey, accountEntity.getUniqueKey());
        assertNotNull(accountEntity.getActivationDate());
        assertNotNull(accountEntity.getCreatedAt());
        assertNotNull(accountEntity.getLastInterestCalculationDate());
        verify(accountRepository, times(1)).save(accountEntity);

    }

 */
    @DisplayName("createAccount -- Failure Scenario")
    @Test
    void Test_When_createAccount_Failure() {
        // Arrange
        AccountReqDto accountReqDto =new AccountReqDto(); // create an AccountReqDto for testing

                // Mocking the behavior of the mapper to return null
                when(accountMapper.toReq(accountReqDto)).thenReturn(null);

        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            accountService.createAccount(accountReqDto);
        });
        verify(accountRepository, never()).save(any());
    }

    private Account getMockAccount() {
        return Account.builder()
                .id(1)
                .accountHolderType(Account.AccountHolderType.CUS)
                .accountHolderCode("AHO1451")
                .activationDate(new Date())
                .allowOverdraft(true)
                .availableBalance(BigDecimal.valueOf(5000.00))
                .blockedBalance(BigDecimal.valueOf(100.00))
                .clientUk("64e15cd5f8946b530d50a604")
                .closedDate(new Date())
                .codeInternalAccount("12052214")
                .codeInternationalAccount("EC11035412052214")
                .createdAt(new Date())
                .createdBy("DAYANNA SILVA")
                .interestRate(BigDecimal.valueOf(0.01))
                .lastInterestCalculationDate(new Date())
                .maxOverdraft(BigDecimal.valueOf(0))
                .name("AHO1451")
                .productUk("64e16953b6708eda0f59aab8")
                .state(Account.State.ACT)
                .totalBalance(BigDecimal.valueOf(5000.00))
                .valid(true)
                .build();
    }

    private List<Account> getMockListAccounts() {
        List<Account> accountList = new ArrayList<>();

        Account account1 = Account.builder()
                .id(1)
                .accountHolderType(Account.AccountHolderType.CUS)
                .accountHolderCode("AHO1451")
                .activationDate(new Date())
                .allowOverdraft(true)
                .availableBalance(BigDecimal.valueOf(5000.00))
                .blockedBalance(BigDecimal.valueOf(100.00))
                .clientUk("64e15cd5f8946b530d50a604")
                .closedDate(new Date())
                .codeInternalAccount("12052214")
                .codeInternationalAccount("EC11035412052214")
                .createdAt(new Date())
                .createdBy("DAYANNA SILVA")
                .interestRate(BigDecimal.valueOf(0.01))
                .lastInterestCalculationDate(new Date())
                .maxOverdraft(BigDecimal.valueOf(0))
                .name("AHO1451")
                .productUk("64e16953b6708eda0f59aab8")
                .state(Account.State.ACT)
                .totalBalance(BigDecimal.valueOf(5000.00))
                .valid(true)
                .build();

        Account account2 = Account.builder()
                .id(2)
                .accountHolderType(Account.AccountHolderType.CUS)
                .accountHolderCode("AHO2451")
                .activationDate(new Date())
                .allowOverdraft(false)
                .availableBalance(BigDecimal.valueOf(10000.00))
                .blockedBalance(BigDecimal.valueOf(200.00))
                .clientUk("64e15cd5f8946b530d50a605")
                .closedDate(new Date())
                .codeInternalAccount("12053333")
                .codeInternationalAccount("EC11035412053333")
                .createdAt(new Date())
                .createdBy("JOHN DOE")
                .interestRate(BigDecimal.valueOf(0.015))
                .lastInterestCalculationDate(new Date())
                .maxOverdraft(BigDecimal.valueOf(500))
                .name("AHO2451")
                .productUk("64e16953b6708eda0f59aab9")
                .state(Account.State.ACT)
                .totalBalance(BigDecimal.valueOf(9800.00))
                .valid(true)
                .build();

        accountList.add(account1);
        accountList.add(account2);

        return accountList;
    }


}