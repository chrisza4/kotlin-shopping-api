package com.example.shopping_api.domain.repositories

import com.example.shopping_api.domain.entities.PurchaseRequest
import com.example.shopping_api.domain.entities.PurchaseRequestItem
import com.example.shopping_api.domain.entities.User
import org.springframework.data.repository.CrudRepository
import java.util.*


interface PurchaseRequestRepository : CrudRepository<PurchaseRequest, Long?> {
    fun findByOwner(owner: User): Set<PurchaseRequest>
    fun findFirstByOwnerAndId(owner: User, id: Long): PurchaseRequest?
}
interface PurchaseRequestItemRepository : CrudRepository<PurchaseRequestItem, UUID?> {}