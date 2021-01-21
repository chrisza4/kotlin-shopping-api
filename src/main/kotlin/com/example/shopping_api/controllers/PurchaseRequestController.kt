package com.example.shopping_api.controllers

import com.example.shopping_api.domain.entities.CreatePurchaseInfo
import com.example.shopping_api.domain.entities.PurchaseRequestDetail
import com.example.shopping_api.domain.entities.PurchaseRequestInfo
import com.example.shopping_api.domain.entities.PurchaseRequestItemInfo
import com.example.shopping_api.domain.usecases.PurchaseRequestUseCase
import com.example.shopping_api.hacktools.HackTools
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

data class CreatePurchaseBody(val info: CreatePurchaseInfo, val items: List<PurchaseRequestItemInfo>)
@RestController
class PurchaseRequestController @Autowired constructor(val purchaseRequestUseCase: PurchaseRequestUseCase, val hackTools: HackTools) {

    @GetMapping("/purchase_request")
    fun list(): List<PurchaseRequestInfo> {
        val chris = hackTools.getChris()
        return purchaseRequestUseCase.list(chris, 0, 10)
    }

    @PostMapping("/purchase_request")
    fun create(@RequestBody body: CreatePurchaseBody): PurchaseRequestDetail {
        val chris = hackTools.getChris()
        return purchaseRequestUseCase.create(chris, body.info, body.items)
    }

    @GetMapping("/purchase_request/{id}")
    fun get(@PathVariable id: Long): PurchaseRequestDetail? {
        val chris = hackTools.getChris()
        return purchaseRequestUseCase.get(chris, id)
    }

    @PostMapping("/purchase_request/{id}/approve")
    fun approve(@PathVariable id: Long): PurchaseRequestDetail {
        val mark = hackTools.getMark()
        return purchaseRequestUseCase.approve(mark, id)
    }

    @PostMapping("/purchase_request/{id}/reject")
    fun reject(@PathVariable id: Long): PurchaseRequestDetail {
        val mark = hackTools.getMark()
        return purchaseRequestUseCase.reject(mark, id)
    }

    @PostMapping("/purchase_request/{id}/negotiate")
    fun negotiate(@PathVariable id: Long): PurchaseRequestDetail {
        val mark = hackTools.getMark()
        return purchaseRequestUseCase.negotiate(mark, id)
    }
}