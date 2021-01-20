package com.example.shopping_api.domain.repositories

import com.example.shopping_api.domain.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long?> {
    fun findFirstByUsername(username: String): User
}