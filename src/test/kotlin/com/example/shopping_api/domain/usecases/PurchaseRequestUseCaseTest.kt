package com.example.shopping_api.domain.usecases

import com.example.shopping_api.domain.entities.*
import com.example.shopping_api.domain.repositories.PurchaseRequestItemRepository
import com.example.shopping_api.domain.repositories.PurchaseRequestRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class PurchaseRequestUseCaseTest @Autowired constructor(
        val entityManager: TestEntityManager,
        purchaseRequestRepository: PurchaseRequestRepository,
        purchaseRequestItemRepository: PurchaseRequestItemRepository) {

    val subject: PurchaseRequestUseCase = PurchaseRequestUseCase(purchaseRequestRepository, purchaseRequestItemRepository)
    val employeeChris = Fixtures.employeeChris()
    val employeeJames = Fixtures.employeeJames()
    val managerMark = Fixtures.managerMark()

    @BeforeEach
    fun setup() {
        entityManager.persist(employeeChris)
        entityManager.persist(employeeJames)
        entityManager.flush()
    }

    @Test
    fun `An employee should be able to create a purchase request`() {
        val createResult = subject.create(employeeChris, CreatePurchaseInfo("New Laptops"), listOf(
                PurchaseRequestItemInfo(null, "MacBook", 80000),
                PurchaseRequestItemInfo(null, "Lenovo IdeaGame", 30000)
        ))
        assertThat(createResult.items.size).isEqualTo(2)
        assertThat(createResult.items[0].id).isNotNull
        assertThat(createResult.items[0].description).isEqualTo("MacBook")
        assertThat(createResult.items[1].id).isNotNull
        assertThat(createResult.items[1].description).isEqualTo("Lenovo IdeaGame")

        val result = subject.list(employeeChris, 0, 10)
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].reason).isEqualTo("New Laptops")
        assertThat(result[0].id).isEqualTo(createResult.id)
    }

    @Test
    fun `An employee should see only their purchase requests`() {
        subject.create(employeeChris, CreatePurchaseInfo("New Laptops"), listOf(
                PurchaseRequestItemInfo(null, "MacBook", 80000),
                PurchaseRequestItemInfo(null, "Lenovo IdeaGame", 30000)
        ))
        subject.create(employeeJames, CreatePurchaseInfo("New Headphones"), listOf(
                PurchaseRequestItemInfo(null, "Sony", 3000),
        ))
        val prsChrisSee = subject.list(employeeChris, 0, 10)
        val prsJamesSee = subject.list(employeeJames, 0, 10)

        assertThat(prsChrisSee.size).isEqualTo(1)
        assertThat(prsChrisSee[0].reason).isEqualTo("New Laptops")

        assertThat(prsJamesSee.size).isEqualTo(1)
        assertThat(prsJamesSee[0].reason).isEqualTo("New Headphones")
    }

    @Test
    fun `The manager should see every purchase requests`() {
        subject.create(employeeChris, CreatePurchaseInfo("New Laptops"), listOf(
                PurchaseRequestItemInfo(null, "MacBook", 80000),
                PurchaseRequestItemInfo(null, "Lenovo IdeaGame", 30000)
        ))
        subject.create(employeeJames, CreatePurchaseInfo("New Headphones"), listOf(
                PurchaseRequestItemInfo(null, "Sony", 3000),
        ))

        val prsMarkSee = subject.list(managerMark, 0, 10)
        assertThat(prsMarkSee.size).isEqualTo(2)
    }

    @Test
    fun `An employee should be able to only get their PR`() {
        val chrisPr = subject.create(employeeChris, CreatePurchaseInfo("New Laptops"), listOf(
                PurchaseRequestItemInfo(null, "MacBook", 80000),
                PurchaseRequestItemInfo(null, "Lenovo IdeaGame", 30000)
        ))
        val jamesPr = subject.create(employeeJames, CreatePurchaseInfo("New Headphones"), listOf(
                PurchaseRequestItemInfo(null, "Sony", 3000),
        ))

        val chrisPrGet = subject.get(employeeChris, chrisPr.id)
        assertThat(chrisPrGet).isNotNull
        assertThat(chrisPrGet?.owner).isEqualTo(employeeChris.username)

        val jamesPrGet = subject.get(employeeJames, jamesPr.id)
        assertThat(jamesPrGet).isNotNull
        assertThat(jamesPrGet?.owner).isEqualTo(employeeJames.username)

        assertThat(subject.get(employeeJames, chrisPr.id)).isNull()
        assertThat(subject.get(employeeChris, jamesPr.id)).isNull()
    }
}