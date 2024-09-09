package com.emce.paymentservice.controller;

import com.emce.commons.dto.CreditResponse;
import com.emce.paymentservice.dto.PaymentRequest;
import com.emce.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<CreditResponse> payInstallment(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(paymentService.pay(paymentRequest));
    }


}
