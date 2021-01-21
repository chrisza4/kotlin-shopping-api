package com.example.shopping_api.domain.entities

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.transaction.NotSupportedException

class PurchaseRequestTest {
    @Test
    fun `Non-approved PR can be approved`() {
        val manager = Fixtures.Users.managerMark()
        val pr = Fixtures.PurchaseRequests.simplePr()
        val approvedAt = ZonedDateTime.of(2020, 10, 1, 12, 0, 0, 0, ZoneId.systemDefault())
        pr.approve(manager, approvedAt)
        assertThat(pr.approver?.username).isEqualTo(manager.username)
        assertThat(pr.approvedAt).isEqualTo(approvedAt)
        assertThat(pr.status).isEqualTo(PurchaseRequestStatus.Approved)
    }

    @Test
    fun `Already approved PR cannot be re-approved`() {
        val manager = Fixtures.Users.managerMark()
        val pr = Fixtures.PurchaseRequests.approvedPr()
        val approvedAt = ZonedDateTime.of(2020, 10, 1, 12, 0, 0, 0, ZoneId.systemDefault())

        assertThrows<NotSupportedException> {
            pr.approve(manager, approvedAt)
        }
    }

    @Test
    fun `An employee cannot approved PR`() {
        val employee = Fixtures.Users.employeeChris()
        val pr = Fixtures.PurchaseRequests.simplePr()
        val approvedAt = ZonedDateTime.of(2020, 10, 1, 12, 0, 0, 0, ZoneId.systemDefault())

        assertThrows<NotSupportedException> {
            pr.approve(employee, approvedAt)
        }
    }

    @Test
    fun `Can reject PR`() {
        val manager = Fixtures.Users.managerMark()
        val pr = Fixtures.PurchaseRequests.simplePr()

        pr.reject(manager)

        assertThat(pr.approver?.username).isEqualTo(manager.username)
        assertThat(pr.status).isEqualTo(PurchaseRequestStatus.Rejected)
    }

    @Test
    fun `Already approved PR cannot be reject`() {
        val manager = Fixtures.Users.managerMark()
        val pr = Fixtures.PurchaseRequests.approvedPr()

        assertThrows<NotSupportedException> {
            pr.reject(manager)
        }
    }

    @Test
    fun `Employee cannot reject PR reject`() {
        val employee = Fixtures.Users.employeeChris()
        val pr = Fixtures.PurchaseRequests.simplePr()

        assertThrows<NotSupportedException> {
            pr.reject(employee)
        }
    }

    @Test
    fun `Can negotiate PR`() {
        val manager = Fixtures.Users.managerMark()
        val pr = Fixtures.PurchaseRequests.simplePr()

        pr.negotiate(manager)

        assertThat(pr.approver?.username).isEqualTo(manager.username)
        assertThat(pr.status).isEqualTo(PurchaseRequestStatus.Negotiated)
    }

    @Test
    fun `Already approved PR cannot be negotiate`() {
        val manager = Fixtures.Users.managerMark()
        val pr = Fixtures.PurchaseRequests.approvedPr()

        assertThrows<NotSupportedException> {
            pr.negotiate(manager)
        }
    }

    @Test
    fun `Employee cannot negotiate PR`() {
        val employee = Fixtures.Users.employeeChris()
        val pr = Fixtures.PurchaseRequests.simplePr()

        assertThrows<NotSupportedException> {
            pr.negotiate(employee)
        }
    }
}