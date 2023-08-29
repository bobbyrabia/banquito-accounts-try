package ec.edu.espe.banquito.accounts.controller;

import ec.edu.espe.banquito.accounts.controller.req.AccountTransactionReqDto;
import ec.edu.espe.banquito.accounts.controller.res.AccountTransactionResDto;
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
@CrossOrigin({"*"})
public class AccountTransactionController {
    private final AccountTransanctionService accountTransanctionService;



@GetMapping("/history-transaction/{accountUK}")
public ResponseEntity<List<AccountTransactionResDto>> findTransactionsByClientUK(
    @PathVariable("accountUK") String accountUK,
    @RequestParam(name = "startDate", required = false) Long startDate,
    @RequestParam(name = "endDate", required = false) Long endDate
){
    List<AccountTransactionResDto> transactions;

    if (startDate != null && endDate != null) {
        transactions = accountTransanctionService.findTransactionsByDateRange(accountUK, new Date(startDate), new Date(endDate));
    } else {
        transactions = accountTransanctionService.findByAccountsTransactionByClientUK(accountUK);
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
