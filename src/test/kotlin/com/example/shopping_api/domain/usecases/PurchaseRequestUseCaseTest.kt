package com.example.shopping_api.domain.usecases

import com.example.shopping_api.domain.entities.CreatePurchaseInfo
import com.example.shopping_api.domain.entities.PurchaseRequestItemInfo
import com.example.shopping_api.domain.entities.Role
import com.example.shopping_api.domain.entities.User
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
    val employeeChris: User = User(username = "Chris", role = Role.Employee)
    val employeeJames: User = User(username = "James", role = Role.Employee)

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
        val chrisPr = subject.create(employeeChris, CreatePurchaseInfo("New Laptops"), listOf(
                PurchaseRequestItemInfo(null, "MacBook", 80000),
                PurchaseRequestItemInfo(null, "Lenovo IdeaGame", 30000)
        ))
        val jamesPr = subject.create(employeeJames, CreatePurchaseInfo("New Headphones"), listOf(
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
    fun `An employee should be able to only get their PR`() {
        val chrisPr = subject.create(employeeChris, CreatePurchaseInfo("New Laptops"), listOf(
                PurchaseRequestItemInfo(null, "MacBook", 80000),
                PurchaseRequestItemInfo(null, "Lenovo IdeaGame", 30000)
        ))
        val jamesPr = subject.create(employeeJames, CreatePurchaseInfo("New Headphones"), listOf(
                PurchaseRequestItemInfo(null, "Sony", 3000),
        ))

        assertThat(subject.get(employeeChris, chrisPr.id)).isNotNull
        assertThat(subject.get(employeeChris, jamesPr.id)).isNull()

        assertThat(subject.get(employeeJames, chrisPr.id)).isNull()
        assertThat(subject.get(employeeJames, jamesPr.id)).isNotNull
    }
}