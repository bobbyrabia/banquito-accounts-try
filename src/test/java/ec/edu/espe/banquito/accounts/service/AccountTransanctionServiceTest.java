package ec.edu.espe.banquito.accounts.service;



import ec.edu.espe.banquito.accounts.controller.req.AccountTransactionReqDto;
import ec.edu.espe.banquito.accounts.controller.res.AccountTransactionResDto;
import ec.edu.espe.banquito.accounts.model.Account;
import ec.edu.espe.banquito.accounts.model.AccountTransaction;
import ec.edu.espe.banquito.accounts.repository.AccountRepository;
import ec.edu.espe.banquito.accounts.repository.AccountTransactionRepository;
import ec.edu.espe.banquito.accounts.service.mapper.AccountTransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountTransanctionServiceTest {
    @Mock
    private AccountTransactionMapper accountTransactionMapper;
    @Mock
    private AccountTransactionRepository accountTransactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private AccountTransanctionService accountTransanctionService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBankTransfer() {
        // Arrange
        AccountTransactionReqDto accountTransactionReqDto =new AccountTransactionReqDto(); // create an AccountTransactionReqDto for testing
        Account accountDebtor = new Account(); // create an Account entity for the debtor
        Account accountCreditor = new Account();    // create an Account entity for the creditor

        Double ammountTmp = (double) 0;
        String reference = "reference123"; // Mock reference value
        AccountTransaction accountTransactionDebtor = new AccountTransaction(); // create an AccountTransaction entity for the debtor
        AccountTransaction accountTransactionCreditor = new AccountTransaction();// create an AccountTransaction entity for the creditor
        AccountTransactionResDto accountTransactionResDto=new AccountTransactionResDto();
        // Mocking the behavior of the repository and mapper
        when(accountRepository.findValidByCodeInternalAccount(accountTransactionReqDto.getDebtorAccount())).thenReturn(Optional.of(accountDebtor));
        when(accountRepository.findValidByCodeInternalAccount(accountTransactionReqDto.getCreditorAccount())).thenReturn(Optional.of(accountCreditor));
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenReturn(1L);
        when(accountTransactionMapper.toRes(accountTransactionDebtor)).thenReturn(accountTransactionResDto);

        // Act
        AccountTransactionResDto result = accountTransanctionService.bankTransfer(accountTransactionReqDto);

        // Assert
        assertNotNull(result);
        // Add more assertions here if needed
        verify(accountRepository, times(1)).findValidByCodeInternalAccount(accountTransactionReqDto.getDebtorAccount());
        verify(accountRepository, times(1)).findValidByCodeInternalAccount(accountTransactionReqDto.getCreditorAccount());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Long.class));
        verify(accountTransactionRepository, times(2)).save(any());
        verify(accountRepository, times(2)).save(any());
        verify(accountTransactionMapper, times(1)).toRes(accountTransactionDebtor);
    }
    @Test
    public void testBankTransfer_FAILURE() {
        // Arrange
        AccountTransactionReqDto accountTransactionReqDto = new AccountTransactionReqDto(); // create an AccountTransactionReqDto for testing

        // Mocking the behavior of the repository to return an empty optional
        when(accountRepository.findValidByCodeInternalAccount(accountTransactionReqDto.getDebtorAccount())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            accountTransanctionService.bankTransfer(accountTransactionReqDto);
        });
        // Add more verifications if needed
    }




}