package com.squad1.hackathon.api;

import com.squad1.hackathon.entity.Account;
import com.squad1.hackathon.entity.MortgageDetail;
import com.squad1.hackathon.entity.SavingDetails;
import com.squad1.hackathon.entity.User;
import com.squad1.hackathon.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/api-docs/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/savings/customers")
    public ResponseEntity<List<User>> getSavingsAccountCustomers() {
        return ResponseEntity.ok(adminService.getAllSavingsAccountUsers());
    }

    @PostMapping("/mortgage")
    public ResponseEntity<MortgageDetail> createMortgageDetail(@RequestBody MortgageDetail mortgageDetail) {
        return ResponseEntity.ok(adminService.createMortgageDetail(mortgageDetail));
    }

    @PutMapping("/savings/update/{accountNumber}")
    public ResponseEntity<SavingDetails> updateSavingsAccount(@PathVariable String accountNumber, @RequestParam double balance) {
        return ResponseEntity.ok(adminService.updateSavingsAccount(accountNumber, balance));
    }

    @PostMapping("/link")
    public ResponseEntity<Account> linkAccounts(@RequestParam Integer accountId, @RequestParam String customerId) {
        return ResponseEntity.ok(adminService.linkAccounts(accountId, customerId));
    }
}
