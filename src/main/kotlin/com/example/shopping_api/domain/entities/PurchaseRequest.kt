package com.example.shopping_api.domain.entities

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
class PurchaseRequest(
        @Id @GeneratedValue
        var id: Long? = null,
        var reason: String,
        var createdAt: ZonedDateTime = ZonedDateTime.now(),
        @OneToMany
        var items: MutableList<PurchaseRequestItem> = ArrayList(),
        @ManyToOne
        var owner: User? = null
) {

    companion object {
        fun create(info: CreatePurchaseInfo): PurchaseRequest {
            // This can be optimized
            val result = PurchaseRequest(
                    id = null,
                    reason = info.reason,
                    createdAt = ZonedDateTime.now(),
            )
            return result
        }
    }

    fun toDetail(): PurchaseRequestDetail {
        return PurchaseRequestDetail(id!!, reason, createdAt, items.map { item -> item.toInfo() })
    }

    fun toInfo(): PurchaseRequestInfo {
        return PurchaseRequestInfo(id!!, reason, createdAt)
    }
}

@Entity
class PurchaseRequestItem(@Id @GeneratedValue var id: UUID? = null, val description: String, val price: Long) {
    companion object {
        fun fromInfo(info: PurchaseRequestItemInfo): PurchaseRequestItem {
            return PurchaseRequestItem(id = info.id, description = info.description, price = info.price)
        }
    }

    fun toInfo(): PurchaseRequestItemInfo {
        return PurchaseRequestItemInfo(id, description, price)
    }
}

data class CreatePurchaseInfo(val reason: String)
data class PurchaseRequestItemInfo(val id: UUID? = null, val description: String, val price: Long)

interface BasePurchaseRequestInfo {
    val id: Long
    val reason: String
    val createdAt: ZonedDateTime
}

data class PurchaseRequestInfo(
        override val id: Long,
        override val reason: String,
        override val createdAt: ZonedDateTime
) : BasePurchaseRequestInfo

data class PurchaseRequestDetail(
        override val id: Long,
        override val reason: String,
        override val createdAt: ZonedDateTime,
        val items: List<PurchaseRequestItemInfo>
) : BasePurchaseRequestInfo