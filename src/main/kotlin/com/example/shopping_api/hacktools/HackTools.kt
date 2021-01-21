package com.example.shopping_api.hacktools

import com.example.shopping_api.domain.entities.User
import com.example.shopping_api.domain.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HackTools @Autowired constructor(val userRepository: UserRepository)  {
    fun getChris(): User {
        return userRepository.findFirstByUsername("Chris")
    }

    fun getMark(): User {
        return userRepository.findFirstByUsername("Mark")
    }
}