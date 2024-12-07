package com.squad1.hackathon.api;

import com.squad1.hackathon.dto.TransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "TransactionServices")
@RestController
@RequestMapping("/transaction")
public interface TransactionServices {

    @PostMapping(path = "/transferFunds")
    @Operation(summary = "transferFunds", description = "")
    @ApiResponse(responseCode = "200", description = "Success")
    TransactionDTO transferFunds(@RequestBody TransactionDTO transactionDTO);

}
