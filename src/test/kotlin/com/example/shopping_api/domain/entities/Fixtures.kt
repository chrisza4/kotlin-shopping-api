package com.example.shopping_api.domain.entities

import java.time.ZonedDateTime

object Fixtures {
    object Users {
        fun managerMark() = User(username = "Mark", role = Role.Manager)
        fun employeeChris() = User(username = "Chris", role = Role.Employee)
        fun employeeJames() = User(username = "James", role = Role.Employee)
    }

    object PurchaseRequests {
        fun simplePr() = PurchaseRequest(
                id = null,
                reason = "New Laptop",
                createdAt = ZonedDateTime.now(),
                owner = Users.employeeChris(),
                status = PurchaseRequestStatus.Open
        )
        fun approvedPr() = PurchaseRequest(
                id = null,
                reason = "New Laptop",
                createdAt = ZonedDateTime.now(),
                owner = Users.employeeChris(),
                status = PurchaseRequestStatus.Approved,
                approvedAt = ZonedDateTime.now(),
                approver = Users.managerMark()
        )
    }
}