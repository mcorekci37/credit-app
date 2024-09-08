package com.emce.paymentservice.config;

import com.emce.paymentservice.dto.CreditResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CREDITS-SERVICE", path = "/api/v1/credit")
public interface CreditClient {

    @GetMapping("/{creditId}")
    CreditResponse getCredit(@PathVariable("creditId") Integer creditId);
}
