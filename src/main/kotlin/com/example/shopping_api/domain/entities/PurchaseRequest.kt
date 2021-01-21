package com.example.shopping_api.domain.entities

import org.springframework.data.annotation.ReadOnlyProperty
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*
import javax.transaction.NotSupportedException
import kotlin.collections.ArrayList

enum class PurchaseRequestStatus {
    Open,
    Approved,
    Rejected,
    Negotiated
}

@Entity
class PurchaseRequest(
        @Id @GeneratedValue
        var id: Long? = null,
        var reason: String,
        var createdAt: ZonedDateTime = ZonedDateTime.now(),
        @OneToMany
        var items: MutableList<PurchaseRequestItem> = ArrayList(),
        @ManyToOne
        var owner: User? = null,
        @ManyToOne
        @ReadOnlyProperty var approver: User? = null,
        @ReadOnlyProperty var approvedAt: ZonedDateTime? = null,
        @Enumerated(EnumType.STRING)
        @ReadOnlyProperty var status: PurchaseRequestStatus
) {
    private fun canUserApprove(approver: User): Boolean {
        return approver.role == Role.Manager
    }

    companion object {
        fun create(info: CreatePurchaseInfo): PurchaseRequest {
            return PurchaseRequest(
                    id = null,
                    reason = info.reason,
                    createdAt = ZonedDateTime.now(),
                    status = PurchaseRequestStatus.Open
            )
        }
    }

    fun toDetail(): PurchaseRequestDetail {
        return PurchaseRequestDetail(id!!, reason, createdAt, items.map { item -> item.toInfo() },owner?.username ?: "")
    }

    fun toInfo(): PurchaseRequestInfo {
        return PurchaseRequestInfo(id!!, reason, createdAt, owner?.username ?: "")
    }

    fun approve(approver: User, approvedAt: ZonedDateTime = ZonedDateTime.now()) {
        when (true) {
            this.status == PurchaseRequestStatus.Approved -> throw NotSupportedException("PR already approved")
            !canUserApprove(approver) -> throw NotSupportedException("User does not have a permission to approve")
        }

        this.approver = approver
        this.approvedAt = approvedAt
        this.status = PurchaseRequestStatus.Approved
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
    val owner: String
}

data class PurchaseRequestInfo(
        override val id: Long,
        override val reason: String,
        override val createdAt: ZonedDateTime,
        override val owner: String
) : BasePurchaseRequestInfo

data class PurchaseRequestDetail(
        override val id: Long,
        override val reason: String,
        override val createdAt: ZonedDateTime,
        val items: List<PurchaseRequestItemInfo>, override val owner: String) : BasePurchaseRequestInfo