package com.example.shopping_api.repositories

import com.example.shopping_api.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long?> {
}