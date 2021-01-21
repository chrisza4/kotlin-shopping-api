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
final class PurchaseRequest(
        @Id @GeneratedValue
        var id: Long? = null,
        var reason: String,
        var createdAt: ZonedDateTime = ZonedDateTime.now(),
        @OneToMany
        var items: MutableList<PurchaseRequestItem> = ArrayList(),
        @ManyToOne
        var owner: User? = null,
        initialStatus: PurchaseRequestStatus = PurchaseRequestStatus.Open
) {
    @ManyToOne
    var approver: User? = null
        private set

    var approvedAt: ZonedDateTime? = null
        private set
    @Enumerated(EnumType.STRING)
    var status: PurchaseRequestStatus = PurchaseRequestStatus.Open
        private set

    init {
        status = initialStatus
    }

    private fun canUserApprove(approver: User): Boolean {
        return approver.role == Role.Manager
    }

    companion object {
        fun create(info: CreatePurchaseInfo): PurchaseRequest {
            return PurchaseRequest(
                    id = null,
                    reason = info.reason,
                    createdAt = ZonedDateTime.now(),
                    initialStatus = PurchaseRequestStatus.Open
            )
        }
    }

    private fun assertApproveAble(approver: User) {
        when (true) {
            this.status == PurchaseRequestStatus.Approved -> throw NotSupportedException("PR already approved")
            !canUserApprove(approver) -> throw NotSupportedException("User does not have a permission")
        }
    }

    fun toDetail(): PurchaseRequestDetail {
        return PurchaseRequestDetail(id!!, reason, createdAt, items.map { item -> item.toInfo() }, owner?.username
                ?: "", status)
    }

    fun toInfo(): PurchaseRequestInfo {
        return PurchaseRequestInfo(id!!, reason, createdAt, owner?.username ?: "", status)
    }

    fun approve(approver: User, approvedAt: ZonedDateTime = ZonedDateTime.now()) {
        assertApproveAble(approver)

        this.approver = approver
        this.approvedAt = approvedAt
        this.status = PurchaseRequestStatus.Approved
    }

    fun reject(approver: User) {
        assertApproveAble(approver)

        this.approver = approver
        this.status = PurchaseRequestStatus.Rejected
    }

    fun negotiate(approver: User) {
        assertApproveAble(approver)

        this.approver = approver
        this.status = PurchaseRequestStatus.Negotiated
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
    val status: PurchaseRequestStatus
}

data class PurchaseRequestInfo(
        override val id: Long,
        override val reason: String,
        override val createdAt: ZonedDateTime,
        override val owner: String,
        override val status: PurchaseRequestStatus
) : BasePurchaseRequestInfo

data class PurchaseRequestDetail(
        override val id: Long,
        override val reason: String,
        override val createdAt: ZonedDateTime,
        val items: List<PurchaseRequestItemInfo>,
        override val owner: String,
        override val status: PurchaseRequestStatus
): BasePurchaseRequestInfo