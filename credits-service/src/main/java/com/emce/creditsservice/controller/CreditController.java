package com.emce.creditsservice.controller;

import com.emce.creditsservice.dto.CreditRequest;
import com.emce.creditsservice.dto.CreditResponse;
import com.emce.creditsservice.dto.InstallmentDto;
import com.emce.creditsservice.entity.Credit;
import com.emce.creditsservice.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/credit")
public class CreditController {
    private final CreditService creditService;

    @PostMapping("/apply")
    public ResponseEntity<CreditResponse> demandCredit(@RequestBody CreditRequest creditRequest){
        Credit credit = creditService.createCredit(creditRequest);
        CreditResponse response = CreditResponse.builder()
                .userId(credit.getUserId())
                .creditId(credit.getId())
                .installments(credit.getInstallments().stream().map(InstallmentDto::fromInstallment).collect(Collectors.toSet()))
                .build();
        return ResponseEntity.ok(response);
    }


}
