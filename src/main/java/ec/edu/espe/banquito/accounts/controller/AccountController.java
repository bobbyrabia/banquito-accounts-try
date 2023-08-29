package ec.edu.espe.banquito.accounts.controller;

import ec.edu.espe.banquito.accounts.controller.req.AccountReqDto;
import ec.edu.espe.banquito.accounts.controller.req.AccountTransactionReqDto;
import ec.edu.espe.banquito.accounts.controller.res.AccountResDto;
import ec.edu.espe.banquito.accounts.controller.res.ProductResDto;
import ec.edu.espe.banquito.accounts.controller.res.AccountTransactionResDto;
import ec.edu.espe.banquito.accounts.service.AccountService;
import ec.edu.espe.banquito.accounts.service.AccountTransanctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@CrossOrigin({"*"})
public class AccountController {
    private final RestTemplate restTemplate;
    private final AccountService accountService;
    private final AccountTransanctionService accountTransanctionService;

    @GetMapping("/accounts-client/{clientUK}")
    public ResponseEntity<List<AccountResDto>> findAccountsValidByClientUK(
            @PathVariable("clientUK") String clientUK
    ){
        return ResponseEntity.ok(accountService.findAccountsByClientUK(clientUK));
    }

    @GetMapping("/account/{accountUK}")
    public ResponseEntity<AccountResDto> findAccountsValidByAccountUK(
            @PathVariable("accountUK") String accountUK
    ){
        return ResponseEntity.ok(accountService.findAccountByAccountUK(accountUK));
    }

    @GetMapping("/account-internalcode/{accountInternalCode}")
    public ResponseEntity<AccountResDto> findAccountsValidByAccountInternalCode(
            @PathVariable("accountInternalCode") String accountInternalCode
    ){
        return ResponseEntity.ok(accountService.findAccountByInternalCodeAccount(accountInternalCode));
    }

    @GetMapping("/product/{productUK}")
    public ResponseEntity<ProductResDto> findByRestProduct(@PathVariable("productUK") String productUK)
    {
        String url = "https://banquito-ws-passive-products-zsorzzk5da-uc.a.run.app/api/v1/productAccount/productos/"+productUK;
        ProductResDto productResDto = restTemplate.getForObject(url, ProductResDto.class);
        return ResponseEntity.ok(productResDto);
    }
    

    @PutMapping("/account/{accountUK}/max-overdraft")
    public ResponseEntity<AccountResDto> updateMaxOverdraft(
            @PathVariable("accountUK") String accountUK,
            @RequestParam BigDecimal maxOverdraft
    ){
        
        AccountResDto updatedAccount = accountService.updateMaxOverdraft(accountUK, maxOverdraft);
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping
    public ResponseEntity createAccount(@RequestBody AccountReqDto accountRQ){
        this.accountService.createAccount(accountRQ);
        return ResponseEntity.ok().build();
    }


    

}
