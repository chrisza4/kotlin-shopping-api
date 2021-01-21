package com.example.shopping_api.domain.usecases

import com.example.shopping_api.domain.entities.*
import com.example.shopping_api.domain.repositories.PurchaseRequestItemRepository
import com.example.shopping_api.domain.repositories.PurchaseRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

@Component
class PurchaseRequestUseCase @Autowired constructor(
        private val purchaseRequestRepository: PurchaseRequestRepository,
        private val purchaseRequestItemRepository: PurchaseRequestItemRepository
) {
    fun list(user: User, start: Int, count: Int): List<PurchaseRequestInfo> {
        if (user.role == Role.Manager) {
            return purchaseRequestRepository.findAll().toList().map { p -> p.toInfo() }
        }
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

    fun approve(user: User, purchaseRequestId: Long): PurchaseRequestDetail {
        val pr = purchaseRequestRepository.findById(purchaseRequestId).get()
        pr.approve(user, ZonedDateTime.now())
        purchaseRequestRepository.save(pr)
        return pr.toDetail()
    }

    fun reject(user: User, purchaseRequestId: Long): PurchaseRequestDetail {
        val pr = purchaseRequestRepository.findById(purchaseRequestId).get()
        pr.reject(user)
        purchaseRequestRepository.save(pr)
        return pr.toDetail()
    }

    fun negotiate(user: User, purchaseRequestId: Long): PurchaseRequestDetail {
        val pr = purchaseRequestRepository.findById(purchaseRequestId).get()
        pr.negotiate(user)
        purchaseRequestRepository.save(pr)
        return pr.toDetail()
    }
}