package com.squad1.hackathon.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class MortgageDetailRequest {
    private Double propertyCost;
    private Double depositAmount;
}
