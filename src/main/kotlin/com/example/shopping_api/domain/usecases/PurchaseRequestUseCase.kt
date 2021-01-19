package com.example.shopping_api.domain.usecases

import com.example.shopping_api.domain.entities.*
import com.example.shopping_api.domain.repositories.PurchaseRequestItemRepository
import com.example.shopping_api.domain.repositories.PurchaseRequestRepository
import org.springframework.beans.factory.annotation.Autowired

class PurchaseRequestUseCase @Autowired constructor(
        private val purchaseRequestRepository: PurchaseRequestRepository,
        private val purchaseRequestItemRepository: PurchaseRequestItemRepository
) {
    fun list(user: User, start: Int, count: Int): List<PurchaseRequestInfo> {
        return purchaseRequestRepository.findByOwner(user).toList().map { p -> p.toInfo() }
    }

    fun get(user: User, purchaseRequestId: Long): PurchaseRequestDetail? {
        return purchaseRequestRepository.findFirstByOwnerAndId(user, purchaseRequestId)?.toDetail()
    }

    fun create(user: User, info: CreatePurchaseInfo, items: List<PurchaseRequestItemInfo>): PurchaseRequestDetail {
        val toCreate = PurchaseRequest.create(info)
        toCreate.owner = user
        val itemEntities = purchaseRequestItemRepository.saveAll(items.map { i -> PurchaseRequestItem.fromInfo(i) })
        toCreate.items = itemEntities.toMutableList()

        val result = this.purchaseRequestRepository.save(toCreate)
        return result.toDetail()
    }

//    fun approve(user: User, purchaseRequestId: Long) {
//        throw NotImplementedError()
//    }
//
//    fun reject(user: User, purchaseRequestId: Long) {
//        throw NotImplementedError()
//    }
//
//    fun negotiate(user: User, purchaseRequestId: Long) {
//        throw NotImplementedError()
//    }
}