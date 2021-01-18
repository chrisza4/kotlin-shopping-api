package com.example.shopping_api.repositories

import com.example.shopping_api.domain.PurchaseRequest
import com.example.shopping_api.domain.PurchaseRequestItem
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import java.util.*

@DataJpaTest
class RepositoriesTests @Autowired constructor(val purchaseRequestRepository: PurchaseRequestRepository) {
    @Test
    fun `When findByIdOrNull then return Purchase request`() {
        val myPr = PurchaseRequest(reason = "haha")
        val item = PurchaseRequestItem(UUID.randomUUID(), "Another item", 2000)
        val item2 = PurchaseRequestItem(UUID.randomUUID(), "Another item", 2000)
        myPr.items.add(item)
        myPr.items.add(item2)
        purchaseRequestRepository.save(myPr)
        val found = purchaseRequestRepository.findByIdOrNull(myPr.id)
        if (found != null) {
            assertThat(found).isEqualTo(myPr)
            assertThat(found.createdAt).isEqualTo(myPr.createdAt)
            assertThat(found.items.count()).isEqualTo(2)
        } else {
            fail("Not found")
        }
    }
}