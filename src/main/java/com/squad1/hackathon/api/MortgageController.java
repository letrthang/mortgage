package com.squad1.hackathon.api;

import com.squad1.hackathon.dto.MortgageDetailRequest;
import com.squad1.hackathon.entity.MortgageDetail;
import com.squad1.hackathon.service.MortgageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v3/api-docs/mortgage")
public class MortgageController {
    @Autowired
    private MortgageDetailService mortgageService;

    @PostMapping("/create")
    public ResponseEntity<MortgageDetail> createMortgage(@RequestBody MortgageDetailRequest mortgageDetail) {
        return ResponseEntity.ok(mortgageService.createMortgage(mortgageDetail));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String accountNumber, @RequestParam String password) {
        try {
            String result = mortgageService.validateLogin(accountNumber, password);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}