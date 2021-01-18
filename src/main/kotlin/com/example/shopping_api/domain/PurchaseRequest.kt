package com.example.shopping_api.domain

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
        var items: MutableList<PurchaseRequestItem> = ArrayList()
)

@Entity
class PurchaseRequestItem(@Id var id: UUID? = null, description: String, price: Long)