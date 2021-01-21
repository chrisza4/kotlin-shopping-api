package com.example.shopping_api.controllers

import com.example.shopping_api.domain.entities.*
import com.example.shopping_api.domain.usecases.PurchaseRequestUseCase
import com.example.shopping_api.hacktools.HackTools
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.security.Principal

data class CreatePurchaseBody(val info: CreatePurchaseInfo, val items: List<PurchaseRequestItemInfo>)

@RestController
class PurchaseRequestController @Autowired constructor(val purchaseRequestUseCase: PurchaseRequestUseCase, val hackTools: HackTools) {

    @GetMapping("/purchase_request")
    fun list(principal: Principal): List<PurchaseRequestInfo> {
        val user = ((principal as UsernamePasswordAuthenticationToken).credentials as User)
        return purchaseRequestUseCase.list(user, 0, 10)
    }

    @PostMapping("/purchase_request")
    fun create(@RequestBody body: CreatePurchaseBody, principal: Principal): PurchaseRequestDetail {
        val user = ((principal as UsernamePasswordAuthenticationToken).credentials as User)
        return purchaseRequestUseCase.create(user, body.info, body.items)
    }

    @GetMapping("/purchase_request/{id}")
    fun get(@PathVariable id: Long, principal: Principal): PurchaseRequestDetail? {
        val user = ((principal as UsernamePasswordAuthenticationToken).credentials as User)
        return purchaseRequestUseCase.get(user, id)
    }

    @PostMapping("/purchase_request/{id}/approve")
    fun approve(@PathVariable id: Long, principal: Principal): PurchaseRequestDetail {
        val user = ((principal as UsernamePasswordAuthenticationToken).credentials as User)
        return purchaseRequestUseCase.approve(user, id)
    }

    @PostMapping("/purchase_request/{id}/reject")
    fun reject(@PathVariable id: Long, principal: Principal): PurchaseRequestDetail {
        val user = ((principal as UsernamePasswordAuthenticationToken).credentials as User)
        return purchaseRequestUseCase.reject(user, id)
    }

    @PostMapping("/purchase_request/{id}/negotiate")
    fun negotiate(@PathVariable id: Long, principal: Principal): PurchaseRequestDetail {
        val user = ((principal as UsernamePasswordAuthenticationToken).credentials as User)
        return purchaseRequestUseCase.negotiate(user, id)
    }
}