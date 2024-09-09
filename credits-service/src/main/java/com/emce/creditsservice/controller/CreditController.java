package com.emce.creditsservice.controller;

import com.emce.creditsservice.dto.CreditRequest;
import com.emce.creditsservice.service.CreditService;
import com.emce.commons.dto.CreditResponse;
import com.emce.commons.entity.Credit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/credit")
public class CreditController {
    private final CreditService creditService;

    @PostMapping("/apply")
    public ResponseEntity<CreditResponse> demandCredit(@RequestBody CreditRequest creditRequest){
        Credit credit = creditService.createCredit(creditRequest);
        var response = CreditResponse.fromEntity(credit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listAll/{userId}")
    public ResponseEntity<List<CreditResponse>> listCreditsForUser(@PathVariable("userId") Integer userId) {
        List<CreditResponse> credits = creditService.listCreditsForUser(userId)
                .stream().map(CreditResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(credits);
    }
    @GetMapping("/list/{userId}")
    public ResponseEntity<Page<CreditResponse>> listCreditsForUser(@PathVariable("userId") Integer userId,
                                                                   @RequestParam(required = false) String status,
                                                                   @RequestParam(defaultValue = "0") String page,
                                                                   @RequestParam(defaultValue = "2") String size,
                                                                   @RequestParam(defaultValue = "id") String sortBy){
        Page<CreditResponse> credits = creditService.listCreditsForUser(userId, status, Integer.valueOf(page), Integer.valueOf(size), sortBy)
                .map(CreditResponse::fromEntity);
        return ResponseEntity.ok(credits);
    }
    @GetMapping("/list/filter/{userId}")
    public ResponseEntity<Page<CreditResponse>> listCreditsForUser(@PathVariable("userId") Integer userId,
                                                                   @RequestParam(required = false) String status,
                                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                   @RequestParam(defaultValue = "0") String page,
                                                                   @RequestParam(defaultValue = "2") String size,
                                                                   @RequestParam(defaultValue = "id") String sortBy){
        Page<CreditResponse> credits = creditService.listCreditsForUser(userId, status, fromDate, toDate, Integer.valueOf(page), Integer.valueOf(size), sortBy)
                .map(CreditResponse::fromEntity);
        return ResponseEntity.ok(credits);
    }

    @GetMapping("/{creditId}")
    public ResponseEntity<CreditResponse> getCredit(@PathVariable("creditId") Integer creditId){
        Credit credit = creditService.getCredit(creditId);
        var response = CreditResponse.fromEntity(credit);
        return ResponseEntity.ok(response);
    }

}
