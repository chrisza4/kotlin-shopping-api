package com.example.shopping_api.domain.repositories

import com.example.shopping_api.domain.entities.Role
import com.example.shopping_api.domain.entities.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class UsersRepositoryTest @Autowired constructor(val userRepository: UserRepository, val entityManager: TestEntityManager) {
    @Test
    fun `When findByIdOrNull then return a User`() {
        val chris = User(username = "Chris", role = Role.Employee)
        val james = User(username = "James", role = Role.Employee)
        val mark = User(username = "Mark", role = Role.Manager)
        entityManager.persist(chris)
        entityManager.persist(james)
        entityManager.persist(mark)
        entityManager.flush()

        val chrisFromDb = userRepository.findByIdOrNull(chris.id)
        val jamesFromDb = userRepository.findByIdOrNull(james.id)
        val markFromDb = userRepository.findByIdOrNull(mark.id)

        assertThat(chrisFromDb).isEqualTo(chris)
        assertThat(jamesFromDb).isEqualTo(james)
        assertThat(markFromDb).isEqualTo(mark)
    }
}