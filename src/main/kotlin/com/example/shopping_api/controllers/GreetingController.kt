package com.example.shopping_api.controllers

import com.example.shopping_api.domain.entities.Role
import com.example.shopping_api.domain.entities.User
import com.example.shopping_api.domain.repositories.UserRepository
import com.example.shopping_api.hacktools.HackTools
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.concurrent.atomic.AtomicLong

data class Greeting(val id: Long, val content: String)
data class Res(val ok: Boolean)

@RestController
class GreetingController @Autowired constructor(val userRepository: UserRepository, val hackTools: HackTools) {

    private val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String, principal: Principal): Greeting {
        val user = ((principal as UsernamePasswordAuthenticationToken).credentials as User)
        return Greeting(counter.incrementAndGet(), "Hello and Welcome, My dear, ${user.username}")
    }

    @GetMapping("/seeds")
    fun seeds(): Res {
        val employeeChris = User(username = "Chris", role = Role.Employee)
        val employeeJames = User(username = "James", role = Role.Employee)
        val managerMark = User(username = "Mark", role = Role.Manager)
        userRepository.saveAll(listOf(employeeChris, employeeJames, managerMark))
        return Res(true)
    }

}