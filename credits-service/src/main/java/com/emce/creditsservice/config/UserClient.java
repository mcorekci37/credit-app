package com.emce.creditsservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AUTH-SERVER", path = "/api/v1/auth")
public interface UserClient {

    @GetMapping("/checkExists/{userId}")
    Boolean checkExistsById(@PathVariable("userId") Integer userId);
}
