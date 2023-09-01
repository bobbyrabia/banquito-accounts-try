package ec.edu.espe.banquito.accounts.controller;

import ec.edu.espe.banquito.accounts.controller.req.AccountTransactionReqDto;
import ec.edu.espe.banquito.accounts.controller.res.AccountTransactionResDto;
import ec.edu.espe.banquito.accounts.exception.CustomException;
import ec.edu.espe.banquito.accounts.model.Account;
import ec.edu.espe.banquito.accounts.service.AccountService;
import ec.edu.espe.banquito.accounts.service.AccountTransanctionService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class AccountTransactionController {
    private final AccountTransanctionService accountTransanctionService;
    private final AccountService accountService;


@GetMapping("/history-transaction/{accountUK}")
public ResponseEntity<List<AccountTransactionResDto>> findTransactionsByClientUK(
    @PathVariable("accountUK") String accountUK,
    @RequestParam(name = "startDate", required = false) Long startDate,
    @RequestParam(name = "endDate", required = false) Long endDate
){
    List<AccountTransactionResDto> transactions;
    Account account=this.accountService.findByUK(accountUK);
    if (startDate != null && endDate != null) {
        transactions = accountTransanctionService.findTransactionsByDateRange(accountUK, new Date(startDate), new Date(endDate));
    } else {
        transactions = accountTransanctionService.findByAccountsTransactionByClientUK(accountUK);
    }
    if(transactions.isEmpty()){
        throw new CustomException("Account with id "+account.getCodeInternalAccount()+"not found",
                "NOT_FOUND",
                404);
    }

    return ResponseEntity.ok(transactions);
}
@PostMapping("/transaction")
    public ResponseEntity<AccountTransactionResDto> transactionAccounts(
            @RequestBody AccountTransactionReqDto accountTransactionReqDto
            ){
        return ResponseEntity.ok(this.accountTransanctionService.bankTransfer(accountTransactionReqDto));
    }




}
