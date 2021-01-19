package com.example.shopping_api.domain.usecases

import com.example.shopping_api.domain.entities.PurchaseRequest
import com.example.shopping_api.domain.entities.PurchaseRequestItem
import com.example.shopping_api.domain.entities.User

class PurchaseRequestUseCase {
    fun createPurchaseRequest(user: User, purchaseRequest: PurchaseRequest, items: List<PurchaseRequestItem>) {
        throw NotImplementedError()
    }

    fun approvePurchaseRequest(user: User, purchaseRequestId: Long) {
        throw NotImplementedError()
    }

    fun rejectPurchaseRequest(user: User, purchaseRequestId: Long) {
        throw NotImplementedError()
    }

    fun negotiatePurchaseRequest(user: User, purchaseRequestId: Long) {
        throw NotImplementedError()
    }
}