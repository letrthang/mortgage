package com.squad1.hackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("from")
    private String accountNumberFrom;

    @JsonProperty("from_type")
    private String accountNumberFromType;

    @JsonProperty("to_type")
    private String accountNumberTo;

    @JsonProperty("to_type")
    private String accountNumberToType;

    @JsonProperty("amount")
    @NotNull
    @DecimalMin(value="0.01", message = "Amount must be greater than 0")
    private Double amount;

    @JsonProperty("remark")
    @Max(value = 200, message = "Remark cannot exceed 200 characters")
    private String remark;
}
