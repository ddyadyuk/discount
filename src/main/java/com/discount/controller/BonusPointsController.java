package com.discount.controller;

import com.discount.dto.BonusPointsResponse;
import com.discount.dto.WithdrawPointsDto;
import com.discount.service.BonusPointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class BonusPointsController {

    private final BonusPointsService bonusPointsService;

    @PostMapping
    public BonusPointsResponse withdrawPoints(@RequestBody @Validated WithdrawPointsDto withdrawPointsDto) {
        return bonusPointsService.withdrawPoints(withdrawPointsDto);
    }

    @GetMapping
    public BonusPointsResponse getAvailablePoints(@RequestParam(name = "clientId") Long clientId) {
        return bonusPointsService.getAvailablePoints(clientId);
    }
}
